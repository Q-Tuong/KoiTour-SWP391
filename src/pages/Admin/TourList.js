import React, { useState, useEffect } from 'react';
import { Table, Button, Modal, Form, Input } from 'antd';
import './styles1.css';

const TourList = () => {
  const [tours, setTours] = useState([]);
  const [editingItem, setEditingItem] = useState(null);
  const [visible, setVisible] = useState(false);
  const [form] = Form.useForm();

  // Fetch data from API
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch('API_ENDPOINT'); // Thay 'API_ENDPOINT' bằng URL của API
        const data = await response.json();
        setTours(data); // Giả sử dữ liệu từ API là mảng các tour
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };

    fetchData();
  }, []);

  const handleEdit = (item) => {
    setEditingItem(item);
    form.setFieldsValue(item);
    setVisible(true);
  };

  const handleDelete = (id) => {
    setTours(tours.filter(tour => tour.id !== id));
  };

  const handleOk = () => {
    form.validateFields().then(values => {
      if (editingItem) {
        const updatedTours = tours.map(tour =>
          tour.id === editingItem.id ? { ...tour, ...values } : tour
        );
        setTours(updatedTours);
        setEditingItem(null);
      } else {
        const newTour = { id: tours.length + 1, ...values };
        setTours([...tours, newTour]);
      }
      setVisible(false);
    });
  };

  const handleCancel = () => {
    setVisible(false);
    setEditingItem(null);
  };

  const columns = [
    {
      title: 'Code',
      dataIndex: 'code',
      key: 'code',
    },
    {
      title: 'Name',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: 'Price',
      dataIndex: 'price',
      key: 'price',
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
      render: (text) => <img src={text} alt="tour" style={{ width: '100px' }} />, // Hiển thị hình ảnh
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
      <h1>Tours Management</h1>
      <Button type="primary" onClick={() => handleEdit(null)}>
        Add Tour
      </Button>
      <Table dataSource={tours} columns={columns} rowKey="id" />

      <Modal title={editingItem ? 'Edit Tour' : 'Add Tour'} visible={visible} onOk={handleOk} onCancel={handleCancel}>
        <Form form={form} layout="vertical">
          <Form.Item name="code" label="Code" rules={[{ required: true, message: 'Please enter code' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="name" label="Name" rules={[{ required: true, message: 'Please enter name' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="price" label="Price" rules={[{ required: true, message: 'Please enter price' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="description" label="Description" rules={[{ required: true, message: 'Please enter description' }]}>
            <Input.TextArea />
          </Form.Item>
          <Form.Item name="image" label="Image" rules={[{ required: true, message: 'Please enter image URL' }]}>
            <Input />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default TourList;
