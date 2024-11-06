import React, { useState, useEffect } from "react";
import { Table, Button, Modal, Form, Input, message, Popconfirm } from "antd";
import { NavLink, useNavigate } from "react-router-dom";
import axios from "axios";
import "./styles1.css";
import FormItem from "antd/es/form/FormItem";
import { DatePicker } from 'antd';

const TourList = () => {
  const [tours, setTours] = useState([]);
  const [loading, setLoading] = useState(true);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const [visible, setVisible] = useState(false); // Modal visibility state
  const [editingItem, setEditingItem] = useState(null); // Item being edited
  const [form] = Form.useForm();
  const navigate = useNavigate();

  const pageSize = 10;

  const fetchTours = async () => {
    const token = localStorage.getItem("token");

    if (!token) {
      navigate("/signin");
      return;
    }

    try {
      const response = await axios.get(
        `http://14.225.212.120:8080/api/tour/get-all`,
        {
          params: {
            page: currentPage - 1,
            size: pageSize,
          },
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      setTours(response.data.content || []);
      setTotalPages(response.data.totalPages || 0);
    } catch (error) {
      console.error("Error fetching tours:", error);
      if (error.response && error.response.status === 401) {
        navigate("/signin");
      }
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTours();
  }, [currentPage]);

  const handleNextPage = () => {
    if (currentPage < totalPages) {
      setCurrentPage(currentPage + 1);
    }
  };

  const handlePreviousPage = () => {
    if (currentPage > 1) {
      setCurrentPage(currentPage - 1);
    }
  };

  const handleEdit = (item) => {
    setEditingItem(item);
    setVisible(true);
    form.setFieldsValue(item || {});
  };

  const handleDelete = async (id) => {
    Modal.confirm({
      title: 'Delete Confirmation',
      content: 'Are you sure you want to delete this tour? This action cannot be undone.',
      okText: 'Yes, delete it',
      okType: 'danger',
      cancelText: 'No, cancel',
      onOk: async () => {
        try {
          const token = localStorage.getItem("token");
          await axios.delete(`http://14.225.212.120:8080/api/tour/delete/${id}`, {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          });
          message.success("Tour deleted successfully");
          setTours(tours.filter((tour) => tour.id !== id));
        } catch (error) {
          console.error("Error deleting tour:", error);
          message.error("Failed to delete tour");
        }
      },
    });
  };

  const handleCancel = () => {
    setVisible(false);
    form.resetFields();
    setEditingItem(null);
  };

  const handleOk = async () => {
    try {
      const values = await form.validateFields();
      const token = localStorage.getItem("token");

      console.log('Form Values:', values);

      const formData = {
        ...values,
        startedAt: values.startedAt?.format('YYYY-MM-DD'),
        price: parseFloat(values.price),
      };

      console.log('Data sending to server:', formData);

      if (editingItem) {
        await axios.put(
          `http://14.225.212.120:8080/api/tour/update/${editingItem.id}`,
          formData,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        message.success("Tour updated successfully");
        setTours(
          tours.map((tour) =>
            tour.id === editingItem.id ? { ...tour, ...formData } : tour
          )
        );
      } else {
        const response = await axios.post(
          `http://14.225.212.120:8080/api/tour/create`,
          formData,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        message.success("Tour created successfully");
        setTours([...tours, response.data]);
      }

      handleCancel();
    } catch (error) {
      console.error('Error details:', {
        message: error.message,
        response: error.response?.data,
        status: error.response?.status,
        headers: error.response?.headers,
      });
      
      if (error.response) {
        message.error(`Server error: ${error.response.data}`);
      } else if (error.request) {
        message.error('Network error. Please check your connection.');
      } else {
        message.error(`Error: ${error.message}`);
      }
    }
  };

  const columns = [
    { title: "ID", dataIndex: "id", key: "id" },
    { title: "Manager ID", dataIndex: "manager_id", key: "manager_id" },
    { title: "Code", dataIndex: "code", key: "code" },
    { title: "Name", dataIndex: "name", key: "name" },
    { title: "Start At", dataIndex: "startedAt", key: "startedAt" },
    { title: "Duration", dataIndex: "duration", key: "duration" },
    { title: "Description", dataIndex: "description", key: "description" },
    {
      title: "Price",
      dataIndex: "price",
      key: "price",
      render: (text) => `$${text.toFixed(2)}`,
    },
    {
      title: "Image",
      dataIndex: "imgUrl",
      key: "imgUrl",
      render: (url) => (
        <img
          src={url}
          alt="Tour"
          style={{ width: 50, height: 50, objectFit: 'cover' }}
          onError={(e) => {
            e.target.src = '/placeholder-image.png';
            e.target.onerror = null;
          }}
        />
      ),
    },
    { title: "Created At", dataIndex: "createAt", key: "createAt" },
    {
      title: "Actions",
      key: "actions",
      render: (_, record) => (
        <div>
          <Button type="link" onClick={() => handleEdit(record)}>
            Edit
          </Button>
          <Popconfirm
            title="Are you sure to delete this tour?"
            onConfirm={() => handleDelete(record.id)}
            okText="Yes"
            cancelText="No"
          >
            <Button type="link" danger>
              Delete
            </Button>
          </Popconfirm>
        </div>
      ),
    },
  ];

  if (loading) {
    return <div>Loading...</div>;
  }

  if (tours.length === 0) {
    return <div>No tours available.</div>;
  }

  return (
    <div>
      <h1>Tours Management</h1>
      <Button type="primary" onClick={() => handleEdit(null)}>
        Add Tour
      </Button>
      <Table
        dataSource={tours}
        columns={columns}
        rowKey="id"
        pagination={false}
      />

      <div className="pagination-controls">
        <Button onClick={handlePreviousPage} disabled={currentPage === 1}>
          Previous
        </Button>
        <span>
          Page {currentPage} of {totalPages}
        </span>
        <Button onClick={handleNextPage} disabled={currentPage === totalPages}>
          Next
        </Button>
      </div>

      <Modal
        title={editingItem ? "Edit Tour" : "Add Tour"}
        open={visible}
        onOk={handleOk}
        onCancel={handleCancel}
      >
        <Form form={form} layout="vertical">
          <FormItem
            name="manager_id"
            label="Manager ID"
            rules={[{
              required: true,
              message: "Please enter manager ID",
            },
            ]}>
            <Input />
          </FormItem>
          <Form.Item
            name="code"
            label="Code"
            rules={[{
              required: true,
              message: "Please enter code"
            },
            {
              pattern: /^DF\d{6}$/,
              message: "Code must start with 'DF' followed by 6 numbers (e.g., DF123456)"
            }
            ]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            name="name"
            label="Name"
            rules={[{ required: true, message: "Please enter name" }]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            name="startedAt"
            label="Start at"
            rules={[
              {
                required: true,
                message: "Please select a start date",
              }
            ]}
          >
            <DatePicker 
              style={{ width: '100%' }}
              format="YYYY-MM-DD"
              placeholder="Select start date"
            />
          </Form.Item>
          <FormItem
            name="duration"
            label="Duration"
            rules={[{ required: true, message: "Please enter duration" }
            ]}
          >
            <Input />
          </FormItem>
          <Form.Item
            name="price"
            label="Price"
            rules={[
              { required: true, message: "Please enter price" },
              {
                pattern: /^[0-9]+(\.[0-9]{1,2})?$/,
                message: "Please enter a valid price (e.g., 99.99)"
              },
              {
                validator: (_, value) => {
                  if (value && value <= 0) {
                    return Promise.reject('Price must be greater than 0');
                  }
                  return Promise.resolve();
                }
              }
            ]}
          >
            <Input 
              type="number"
              min="0"
              step="1"
              placeholder="Enter price (e.g., 99.99)"
              prefix="$"
            />
          </Form.Item>
          <Form.Item
            name="description"
            label="Description"
            rules={[{ required: true, message: "Please enter description" }]}
          >
            <Input.TextArea />
          </Form.Item>
          <Form.Item
            name="imgUrl"
            label="Image"
            rules={[{ required: true, message: "Please enter image URL" }]}
          >
            <Input />
          </Form.Item>
        </Form>
      </Modal>
      <div className="button-container">
        <span>
          <NavLink to="/admin">
            <button className="form-button2 back-button">Back</button>
          </NavLink>
        </span>

      </div>

    </div>
  );
};

export default TourList;
