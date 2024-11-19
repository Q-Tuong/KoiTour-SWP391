import React, { useState, useEffect } from 'react';
import { Table, Button, Modal, Form, Input, message, Select } from 'antd';
import axios from 'axios';
import './styles1.css';
import { NavLink } from 'react-router-dom';

const KoiList = () => {
  const [koi, setKoi] = useState([]);
  const [pageNumber, setPageNumber] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [loading, setLoading] = useState(true);
  const [visible, setVisible] = useState(false);
  const [editingItem, setEditingItem] = useState(null);
  const [form] = Form.useForm();
  const [previewImage, setPreviewImage] = useState('');
  const [previewVisible, setPreviewVisible] = useState(false);

  const fetchKoi = async (page) => {
    const token = localStorage.getItem("token");

    setLoading(true); // Set loading to true before fetching data

    try {
      const response = await axios.get(
        "http://14.225.212.120:8080/api/koi/get-all",
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
          params: {
            page: page - 1,
          },
        }
      );

      const data = response.data;
      if (data && data.content) {
        setKoi(data.content);
        setTotalPages(data.totalPages); // Set total pages for pagination
      } else {
        message.error("No data found"); // Show error if no data
      }
    } catch (error) {
      console.error("Error fetching koi:", error);
      message.error("Failed to fetch koi");
    } finally {
      setLoading(false); // Set loading to false after fetching data
    }
  };

  useEffect(() => {
    fetchKoi(pageNumber);
  }, [pageNumber]);

  const handleAddOrUpdate = async (values) => {
    const token = localStorage.getItem("token");
    const url = editingItem
      ? `http://14.225.212.120:8080/api/koi/update/${editingItem.id}`
      : "http://14.225.212.120:8080/api/koi/create";
    const method = editingItem ? "put" : "post";

    try {
      await axios[method](url, values, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      message.success(`Koi ${editingItem ? 'updated' : 'added'} successfully`);
      fetchKoi(pageNumber);
      handleCancel();
    } catch (error) {
      console.error("Error saving koi:", error);
      message.error(`Failed to ${editingItem ? 'update' : 'add'} koi`);
    }
  };

  const handleDelete = async (id) => {
    Modal.confirm({
      title: 'Are you sure you want to delete this koi?',
      content: 'This action cannot be undone.',
      okText: 'Yes, delete it',
      okType: 'danger',
      cancelText: 'No, cancel',
      onOk: async () => {
        const token = localStorage.getItem("token");
        try {
          await axios.delete(`http://14.225.212.120:8080/api/koi/delete/${id}`, {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          });
          message.success("Koi deleted successfully");
          fetchKoi(pageNumber);
        } catch (error) {
          console.error("Error deleting koi:", error);
          message.error("Failed to delete koi");
        }
      },
    });
  };

  const handleCancel = () => {
    setVisible(false);
    form.resetFields();
    setEditingItem(null);
  };

  const columns = [
    { title: 'Name', dataIndex: 'name', key: 'name' },
    { title: 'Type', dataIndex: 'type', key: 'type' },
    { title: 'Size', dataIndex: 'size', key: 'size' },
    { title: 'Origin', dataIndex: 'origin', key: 'origin' },
    { 
      title: 'Image', 
      dataIndex: 'imgUrl', 
      key: 'imgUrl',
      render: (imgUrl) => (
        <img 
          src={imgUrl} 
          alt="koi" 
          style={{ 
            width: '120px', 
            height: '120px', 
            objectFit: 'cover', 
            cursor: 'pointer',
            borderRadius: '8px',
            transition: 'transform 0.3s ease',
            boxShadow: '0 2px 4px rgba(0,0,0,0.1)'
          }} 
          onClick={() => {
            setPreviewImage(imgUrl);
            setPreviewVisible(true);
          }}
        />
      )
    },
    { 
      title: 'Price', 
      dataIndex: 'price', 
      key: 'price',
      render: (price) => price?.toLocaleString('vi-VN') + ' ₫'
    },
    { title: 'Create At', dataIndex: 'createAt', key: 'createAt' },
    {
      title: 'Actions',
      render: (text, record) => (
        <div style={{ display: 'flex', gap: '8px' }}>
          <Button onClick={() => { setEditingItem(record); form.setFieldsValue(record); setVisible(true); }}>
            Edit
          </Button>
          <Button danger onClick={() => handleDelete(record.id)}>
            Delete
          </Button>
        </div>
      ),
    },
  ];

  return (
    <div>
      <h1>Koi Management</h1>
      <Button type="primary" onClick={() => setVisible(true)}>
        Add Koi
      </Button>
      <Table 
        dataSource={koi} 
        columns={columns} 
        rowKey="id" 
        loading={loading} 
        pagination={{ 
          current: pageNumber, 
          total: totalPages * 10, 
          onChange: setPageNumber 
        }} 
      />

      <Modal 
        title={editingItem ? 'Edit Koi' : 'Add Koi'} 
        open={visible} 
        onOk={() => form.submit()} 
        onCancel={handleCancel}
      >
        <Form form={form} layout="vertical" onFinish={handleAddOrUpdate}>
          <Form.Item name="name" label="Name" rules={[{ required: true, message: 'Please enter name' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="type" label="Type" rules={[{ required: true, message: 'Please enter type' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="size" label="Size" rules={[{ required: true, message: 'Please enter size' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="origin" label="Origin" rules={[{ required: true, message: 'Please enter origin' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="price" label="Price" rules={[{ required: true, message: 'Please enter price' }]}>
            <Input 
            type="number"
            min="0"
            step="1"
            placeholder="Enter price (e.g., 99.99)"
            prefix="$"
            />
          </Form.Item>
          <Form.Item 
            name="imgUrl"
            label="Image URL"
            rules={[{ required: true, message: 'Please enter image URL' }]}
          >
            <Input />
          </Form.Item>
        </Form>
      </Modal>
      
      <Modal
        open={previewVisible}
        footer={null}
        onCancel={() => setPreviewVisible(false)}
        width={800}
        style={{ top: 20 }}
        bodyStyle={{ padding: '20px' }}
      >
        <img
          alt="Preview"
          style={{
            width: '100%',
            maxHeight: '80vh',
            objectFit: 'contain'
          }}
          src={previewImage}
        />
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

export default KoiList;
