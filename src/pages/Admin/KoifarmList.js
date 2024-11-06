import React, { useState, useEffect } from 'react';
import { Table, Button, Modal, Form, Input, message } from 'antd';
import axios from 'axios';
import './styles1.css';
import { NavLink } from 'react-router-dom';

const KoifarmList = () => {
  const [koiFarms, setKoiFarms] = useState([]);
  const [pageNumber, setPageNumber] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);
  const [visible, setVisible] = useState(false);
  const [editingItem, setEditingItem] = useState(null);
  const [form] = Form.useForm();

  const fetchKoiFarms = async (page) => {
    const token = localStorage.getItem("token"); 
    setLoading(true);

    try {
      const response = await axios.get(
        `http://14.225.212.120:8080/api/koiFarm/get-all`,
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
      setKoiFarms(data.content);
      setTotalPages(data.totalPages);
    } catch (error) {
      console.error("Error fetching koi farms:", error);
      message.error("Failed to fetch koi farms.");
    } finally {
      setLoading(false);
    }
  };


  useEffect(() => {
    fetchKoiFarms(pageNumber); 
  }, [pageNumber]);

  const handleAddOrUpdate = async (values) => {
    const token = localStorage.getItem("token");
    const url = editingItem
      ? `http://14.225.212.120:8080/api/koiFarm/update/${editingItem.id}`
      : `http://14.225.212.120:8080/api/koiFarm/create`;
    const method = editingItem ? "put" : "post";

    try {
      await axios[method](url, values, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      message.success(`Koi Farm ${editingItem ? 'updated' : 'added'} successfully`);
      fetchKoiFarms(pageNumber);
      handleCancel();
    } catch (error) {
      message.error(`Failed to ${editingItem ? 'update' : 'add'} koi farm.`);
    }
  };

  const handleDelete = async (id) => {
    Modal.confirm({
      title: 'Delete Confirmation',
      content: 'Are you sure you want to delete this koi farm? This action cannot be undone.',
      okText: 'Yes, delete it',
      okType: 'danger',
      cancelText: 'No, cancel',
      onOk: async () => {
        const token = localStorage.getItem("token");
        try {
          await axios.delete(`http://14.225.212.120:8080/api/koiFarm/delete/${id}`, {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          });
          message.success("Koi Farm deleted successfully");
          fetchKoiFarms(pageNumber);
        } catch (error) {
          message.error("Failed to delete koi farm");
        }
      },
    });
  };

  const handleEdit = (item) => {
    setEditingItem(item);
    form.setFieldsValue(item);
    setVisible(true);
  };

  const handleCancel = () => {
    setVisible(false);
    form.resetFields();
    setEditingItem(null);
  };

  const columns = [
    { title: 'Name', dataIndex: 'name', key: 'name' },
    { title: 'Address', dataIndex: 'address', key: 'address' },
    { title: 'Phone', dataIndex: 'phone', key: 'phone' },
    { 
      title: 'Image', 
      dataIndex: 'imgUrl', 
      key: 'imgUrl',
      render: (imgUrl) => (
        <img 
          src={imgUrl} 
          alt="koi farm" 
          className="table-image"
        />
      )
    },
    {
      title: 'Actions',
      render: (text, record) => (
        <>
          <Button onClick={() => handleEdit(record)}>Edit</Button>
          <Button danger onClick={() => handleDelete(record.id)}>Delete</Button>
        </>
      ),
    },
  ];

  return (
    <div>
      <h1>Koi Farms Management</h1>
      <Button type="primary" onClick={() => handleEdit(null)}>
        Add Koi Farm
      </Button>
      <Table dataSource={koiFarms} columns={columns} rowKey="id" loading={loading} pagination={{ current: pageNumber, total: totalPages * 10, onChange: setPageNumber }} />

      <Modal title={editingItem ? 'Edit Koi Farm' : 'Add Koi Farm'} open={visible} onOk={() => form.submit()} onCancel={handleCancel}>
        <Form form={form} layout="vertical" onFinish={handleAddOrUpdate}>
          <Form.Item name="name" label="Name" rules={[{ required: true, message: 'Please enter name' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="address" label="Address" rules={[{ required: true, message: 'Please enter address' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="phone" label="Phone" rules={[
            { required: true, message: 'Please enter phone' },
            {
              pattern: /^\d{10}$/,
              message: 'Please enter a valid phone number (10 digits)'
            }
          ]}>
            <Input />
          </Form.Item>
          <Form.Item name="description" label="Description">
            <Input.TextArea />
          </Form.Item>
          <Form.Item name="imgUrl" label="Image URL" rules={[{ required: true, message: 'Please enter image URL' }]}>
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

export default KoifarmList;
