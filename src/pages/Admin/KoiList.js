import React, { useState, useEffect } from 'react';
import { Table, Button, Modal, Form, Input, message } from 'antd';
import axios from 'axios';
import './styles1.css';

const KoiList = () => {
  const [koiList, setKoiList] = useState([]);
  const [editingItem, setEditingItem] = useState(null);
  const [visible, setVisible] = useState(false);
  const [form] = Form.useForm();

  // Fetch koi data from API
  useEffect(() => {
    const fetchKoiList = async () => {
      try {
        const response = await axios.get('/api/koi'); // API endpoint
        setKoiList(response.data);
      } catch (error) {
        message.error('Failed to load koi data');
      }
    };

    fetchKoiList();
  }, []);

  const handleEdit = (item) => {
    setEditingItem(item);
    form.setFieldsValue(item || {}); // Set form values
    setVisible(true);
  };

  const handleDelete = async (id) => {
    try {
      await axios.delete(`/api/koi/${id}`); // API delete endpoint
      setKoiList(koiList.filter(koi => koi.id !== id));
      message.success('Koi deleted successfully');
    } catch (error) {
      message.error('Failed to delete koi');
    }
  };

  const handleOk = () => {
    form.validateFields().then(async (values) => {
      try {
        if (editingItem) {
          // Update koi
          const updatedKoi = await axios.put(`/api/koi/${editingItem.id}`, values); // API update endpoint
          const updatedKoiList = koiList.map(koi =>
            koi.id === editingItem.id ? { ...koi, ...updatedKoi.data } : koi
          );
          setKoiList(updatedKoiList);
          message.success('Koi updated successfully');
        } else {
          // Add new koi
          const newKoi = await axios.post('/api/koi', values); // API add endpoint
          setKoiList([...koiList, newKoi.data]);
          message.success('Koi added successfully');
        }
        setEditingItem(null);
        setVisible(false);
      } catch (error) {
        message.error('Failed to save koi');
      }
    });
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
      title: 'Farm Name',
      dataIndex: 'farmName',
      key: 'farmName',
    },
    {
      title: 'Type',
      dataIndex: 'type',
      key: 'type',
    },
    {
      title: 'Color',
      dataIndex: 'color',
      key: 'color',
    },
    {
      title: 'Size',
      dataIndex: 'size',
      key: 'size',
    },
    {
      title: 'Origin',
      dataIndex: 'origin',
      key: 'origin',
    },
    {
      title: 'Price',
      dataIndex: 'price',
      key: 'price',
      render: (text) => `$${text}`, // Format price as currency
    },
    {
      title: 'Image',
      dataIndex: 'image',
      key: 'image',
      render: (imageUrl) => (
        <img src={imageUrl} alt="koi" style={{ width: '100px', height: 'auto' }} />
      ),
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
      <h1>Koi Management</h1>
      <Button type="primary" onClick={() => handleEdit(null)}>
        Add Koi
      </Button>
      <Table dataSource={koiList} columns={columns} rowKey="id" />

      <Modal title={editingItem ? 'Edit Koi' : 'Add Koi'} visible={visible} onOk={handleOk} onCancel={handleCancel}>
        <Form form={form} layout="vertical">
          <Form.Item name="name" label="Name" rules={[{ required: true, message: 'Please enter name' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="farmName" label="Farm Name" rules={[{ required: true, message: 'Please enter farm name' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="type" label="Type" rules={[{ required: true, message: 'Please enter type' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="color" label="Color" rules={[{ required: true, message: 'Please enter color' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="size" label="Size" rules={[{ required: true, message: 'Please enter size' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="origin" label="Origin" rules={[{ required: true, message: 'Please enter origin' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="price" label="Price" rules={[{ required: true, message: 'Please enter price' }]}>
            <Input type="number" />
          </Form.Item>
          <Form.Item name="image" label="Image URL" rules={[{ required: true, message: 'Please enter image URL' }]}>
            <Input />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default KoiList;

