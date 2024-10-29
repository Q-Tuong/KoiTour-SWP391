// src/pages/Admin/OrderList.js
import React, { useState, useEffect } from 'react';
import { Table, Button, Modal, Form, Input, message } from 'antd';
import axios from 'axios';
import './styles1.css';

const OrderList = () => {
  const [orders, setOrders] = useState([]);
  const [editingItem, setEditingItem] = useState(null);
  const [visible, setVisible] = useState(false);
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();

  // Fetch data from API
  useEffect(() => {
    const fetchOrders = async () => {
      setLoading(true);
      try {
        const response = await axios.get('/api/orders'); // Replace with your API endpoint
        setOrders(response.data);
      } catch (error) {
        message.error('Failed to load orders data.');
      } finally {
        setLoading(false);
      }
    };

    fetchOrders();
  }, []);

  const handleEdit = (item) => {
    setEditingItem(item);
    form.setFieldsValue({ koiID: item.koiID, quantity: item.quantity });
    setVisible(true);
  };

  const handleDelete = async (id) => {
    try {
      await axios.delete(`/api/orders/${id}`); // Replace with your API endpoint
      setOrders(orders.filter(order => order.koiID !== id));
      message.success('Order deleted successfully');
    } catch (error) {
      message.error('Failed to delete order.');
    }
  };

  const handleOk = async () => {
    try {
      const values = await form.validateFields();
      if (editingItem) {
        // Update existing order
        const updatedOrders = orders.map(order =>
          order.koiID === editingItem.koiID ? { ...order, ...values } : order
        );
        await axios.put(`/api/orders/${editingItem.koiID}`, values); // Replace with your API endpoint
        setOrders(updatedOrders);
        message.success('Order updated successfully');
      } else {
        // Add new order
        const newOrder = { ...values };
        const response = await axios.post('/api/orders', newOrder); // Replace with your API endpoint
        setOrders([...orders, response.data]);
        message.success('Order added successfully');
      }
      setVisible(false);
      setEditingItem(null);
    } catch (error) {
      message.error('Failed to save order.');
    }
  };

  const handleCancel = () => {
    setVisible(false);
    setEditingItem(null);
  };

  const columns = [
    {
      title: 'Koi ID',
      dataIndex: 'koiID',
      key: 'koiID',
    },
    {
      title: 'Quantity',
      dataIndex: 'quantity',
      key: 'quantity',
    },
    {
      title: 'Action',
      key: 'action',
      render: (_, record) => (
        <>
          <Button onClick={() => handleEdit(record)}>Edit</Button>
          <Button onClick={() => handleDelete(record.koiID)} danger>
            Delete
          </Button>
        </>
      ),
    },
  ];

  return (
    <div>
      <h1>Order Management</h1>
      <Button type="primary" onClick={() => handleEdit(null)}>
        Add Order
      </Button>
      <Table dataSource={orders} columns={columns} rowKey="koiID" loading={loading} />

      <Modal title={editingItem ? 'Edit Order' : 'Add Order'} visible={visible} onOk={handleOk} onCancel={handleCancel}>
        <Form form={form} layout="vertical">
          <Form.Item name="koiID" label="Koi ID" rules={[{ required: true, message: 'Please enter Koi ID' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="quantity" label="Quantity" rules={[{ required: true, message: 'Please enter quantity' }]}>
            <Input type="number" min={0} />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default OrderList;
