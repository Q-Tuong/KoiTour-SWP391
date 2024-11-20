// src/pages/Admin/OrderList.js
import React, { useState, useEffect } from 'react';
import { Table, Button, Modal, Form, Input, message, Space } from 'antd';
import axios from 'axios';
import './styles1.css';
import { NavLink } from 'react-router-dom';

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
        const token = localStorage.getItem("token");
        const response = await axios.get(
          'http://14.225.212.120:8080/api/order/get-paid-order/koi',
          {
            headers: {
              'Authorization': `Bearer ${token}`,
              'Accept': '*/*'
            }
          }
        );
        setOrders(response.data);
      } catch (error) {
        console.error('Error fetching orders:', error);
        if (error.response?.status === 401) {
          message.error('Please login to view orders');
        } else {
          message.error('Failed to load orders data.');
        }
      } finally {
        setLoading(false);
      }
    };

    fetchOrders();
  }, []);

  const handleEdit = async (item) => {
    try {
      const token = localStorage.getItem("token");
      const response = await axios.put(
        `http://14.225.212.120:8080/api/order/update-status/${item.id}`,
        { status: 'COMPLETED' },
        {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        }
      );
      
      if (response.data) {
        // Update local state to reflect the change
        setOrders(orders.map(order => 
          order.id === item.id 
            ? { ...order, status: 'COMPLETED' }
            : order
        ));
        message.success('Order status updated to COMPLETED');
      }
    } catch (error) {
      console.error('Error updating order status:', error);
      message.error('Failed to update order status');
    }
  };

  const handleDelete = async (id) => {
    try {
      const token = localStorage.getItem("token");
      await axios.delete(`http://14.225.212.120:8080/api/order/${id}`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      setOrders(orders.filter(order => order.id !== id));
      message.success('Order deleted successfully');
    } catch (error) {
      console.error('Error deleting order:', error);
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
      title: 'Order ID',
      dataIndex: 'id',
      key: 'id',
    },
    {
      title: 'Customer Email',
      dataIndex: 'customerEmail',
      key: 'customerEmail',
    },
    {
      title: 'Address',
      dataIndex: ['customer', 'address'], // Nested path to get address from customer object
      key: 'address',
    },
    {
      title: 'Total',
      dataIndex: 'total',
      key: 'total',
      render: (total) => `$${total}`
    },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
      render: (status) => (
        <span style={{ 
          color: status === 'COMPLETED' ? 'green' : 
                 status === 'PAID' ? 'blue' : 'gray'
        }}>
          {status}
        </span>
      )
    },
    {
      title: 'Created At',
      dataIndex: 'createAt',
      key: 'createAt',
    },
    {
      title: 'Action',
      key: 'action',
      render: (_, record) => (
        <Space size="middle">
          {record.status === 'PAID' && (
            <Button 
              type="primary"
              onClick={() => handleEdit(record)}
            >
              Mark as Completed
            </Button>
          )}
          <Button 
            onClick={() => handleDelete(record.id)} 
            danger
          >
            Delete
          </Button>
        </Space>
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

      <Modal title={editingItem ? 'Edit Order' : 'Add Order'} open={visible} onOk={handleOk} onCancel={handleCancel}>
        <Form form={form} layout="vertical">
          <Form.Item name="koiID" label="Koi ID" rules={[{ required: true, message: 'Please enter Koi ID' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="quantity" label="Quantity" rules={[{ required: true, message: 'Please enter quantity' }]}>
            <Input type="number" min={0} />
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

export default OrderList;
