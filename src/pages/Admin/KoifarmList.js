// src/pages/Admin/KoiFarms.js
import React, { useState, useEffect } from 'react';
import { Table, Button, Modal, Form, Input, message } from 'antd';
import axios from 'axios';
import './styles1.css';

const KoifarmList = () => {
  const [koiFarms, setKoiFarms] = useState([]);
  const [editingItem, setEditingItem] = useState(null);
  const [visible, setVisible] = useState(false);
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();

  // Fetch data from API
  useEffect(() => {
    const fetchKoiFarms = async () => {
      setLoading(true);
      try {
        const response = await axios.get('/api/koifarms'); // Replace with your API endpoint
        setKoiFarms(response.data);
      } catch (error) {
        message.error('Failed to load koi farms data.');
      } finally {
        setLoading(false);
      }
    };

    fetchKoiFarms();
  }, []);

  const handleEdit = (item) => {
    setEditingItem(item);
    form.setFieldsValue(item);
    setVisible(true);
  };

  const handleDelete = async (id) => {
    try {
      await axios.delete(`/api/koifarms/${id}`); // Replace with your API endpoint
      setKoiFarms(koiFarms.filter(farm => farm.id !== id));
      message.success('Koi farm deleted successfully');
    } catch (error) {
      message.error('Failed to delete koi farm.');
    }
  };

  const handleOk = async () => {
    try {
      const values = await form.validateFields();
      if (editingItem) {
        // Update existing koi farm
        const updatedKoiFarms = koiFarms.map(farm =>
          farm.id === editingItem.id ? { ...farm, ...values } : farm
        );
        await axios.put(`/api/koifarms/${editingItem.id}`, values); // Replace with your API endpoint
        setKoiFarms(updatedKoiFarms);
        message.success('Koi farm updated successfully');
      } else {
        // Add new koi farm
        const newFarm = { ...values };
        const response = await axios.post('/api/koifarms', newFarm); // Replace with your API endpoint
        setKoiFarms([...koiFarms, response.data]);
        message.success('Koi farm added successfully');
      }
      setVisible(false);
      setEditingItem(null);
    } catch (error) {
      message.error('Failed to save koi farm.');
    }
  };

  const handleCancel = () => {
    setVisible(false);
    setEditingItem(null);
  };

  const columns = [
    {
      title: 'Name',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: 'Address',
      dataIndex: 'address',
      key: 'address',
    },
    {
      title: 'Phone',
      dataIndex: 'phone',
      key: 'phone',
    },
    {
      title: 'Email',
      dataIndex: 'email',
      key: 'email',
    },
    {
      title: 'Description',
      dataIndex: 'description',
      key: 'description',
    },
    {
      title: 'Image',
      dataIndex: 'image',
      key: 'image',
      render: (image) => <img src={image} alt="Koi Farm" style={{ width: '100px' }} />,
    },
    {
      title: 'Action',
      key: 'action',
      render: (_, record) => (
        <>
          <Button onClick={() => handleEdit(record)}>Edit</Button>
          <Button onClick={() => handleDelete(record.id)} danger>
            Delete
          </Button>
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
      <Table dataSource={koiFarms} columns={columns} rowKey="id" loading={loading} />

      <Modal title={editingItem ? 'Edit Koi Farm' : 'Add Koi Farm'} visible={visible} onOk={handleOk} onCancel={handleCancel}>
        <Form form={form} layout="vertical">
          <Form.Item name="name" label="Name" rules={[{ required: true, message: 'Please enter name' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="address" label="Address" rules={[{ required: true, message: 'Please enter address' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="phone" label="Phone" rules={[{ required: true, message: 'Please enter phone' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="email" label="Email" rules={[{ required: true, message: 'Please enter email' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="description" label="Description">
            <Input.TextArea />
          </Form.Item>
          <Form.Item name="image" label="Image URL" rules={[{ required: true, message: 'Please enter image URL' }]}>
            <Input />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default KoifarmList;
