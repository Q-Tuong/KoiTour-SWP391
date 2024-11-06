import React, { useState, useEffect } from 'react';
import { Table, Button, Modal, Form, Input, Select, message } from 'antd';
import axios from 'axios';
import './styles1.css';
import { NavLink } from 'react-router-dom';

const { Option } = Select;

const UserList = () => {
  const [users, setUsers] = useState([]);
  const [editingItem, setEditingItem] = useState(null);
  const [visible, setVisible] = useState(false);
  const [form] = Form.useForm();

  // Fetch users from API with token
  useEffect(() => {
    const fetchUsers = async () => {
      const token = localStorage.getItem('token'); // Get token from localStorage

      try {
        const response = await axios.get(
          'http://14.225.212.120:8080/api/admin/get-all',
          {
            headers: {
              Authorization: `Bearer ${token}`, // Use token in headers
            },
          }
        );
        setUsers(response.data); // assuming the data is an array of user objects
      } catch (error) {
        console.error('There was an error fetching the users!', error);
      }
    };

    fetchUsers();
  }, []);

  const handleCreate = (item) =>{

  };

  const handleEdit = (item) => {
    setEditingItem(item);
    form.setFieldsValue(item);
    setVisible(true);
  };

  const handleDelete = async (id) => {
    Modal.confirm({
      title: 'Are you sure you want to delete this user?',
      content: 'This action cannot be undone.',
      okText: 'Yes',
      okType: 'danger',
      cancelText: 'No',
      onOk: async () => {
        const token = localStorage.getItem('token');
        try {
          const response = await axios.delete(
            `http://14.225.212.120:8080/api/admin/delete/${id}`,
            {
              headers: {
                Authorization: `Bearer ${token}`,
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*',
              },
              withCredentials: false
            }
          );
          
          if (response.status === 200) {
            message.success('User deleted successfully');
            setUsers(users.filter((user) => user.id !== id));
          } else {
            message.error('Failed to delete user');
          }
        } catch (error) {
          console.error('Error deleting user:', error);
          if (error.response) {
            message.error(`Delete failed: ${error.response.data.message || 'Unknown error'}`);
          } else if (error.request) {
            message.error('No response from server. Please check your connection.');
          } else {
            message.error('Error setting up the request');
          }
        }
      },
    });
  };

  const handleOk = () => {
    form.validateFields().then(async (values) => {
      const token = localStorage.getItem('token'); // Get token from localStorage

      if (editingItem) {
        try {
          // Make the API request to update user information
          const response = await axios.put(
            `http://14.225.212.120:8080/api/user/update/${editingItem.id}`,
            values,
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
          );

          if (response.status === 200) {
            message.success('User updated successfully');

            // Check if the role has changed and update the role if needed
            if (values.role !== editingItem.role) {
              try {
                const roleResponse = await axios.put(
                  `http://14.225.212.120:8080/api/admin/update-role/${editingItem.id}`,
                  { role: values.role },
                  {
                    headers: {
                      Authorization: `Bearer ${token}`,
                    },
                  }
                );

                if (roleResponse.status === 200) {
                  message.success('User role updated successfully');
                } else {
                  message.error('Failed to update user role');
                }
              } catch (roleError) {
                console.error('Error updating user role:', roleError);
                message.error('Error updating user role');
              }
            }

            const updatedUsers = users.map((user) =>
              user.id === editingItem.id ? { ...user, ...values } : user
            );
            setUsers(updatedUsers);
          } else {
            message.error('Failed to update user');
          }
        } catch (error) {
          console.error('Error updating user:', error);
          message.error('Error updating user');
        }
      } else {
        // If no editing item, handle the case of adding a new user here
        const newUser = { id: users.length + 1, ...values };
        setUsers([...users, newUser]);
      }

      setVisible(false);
      setEditingItem(null);
    });
  };

  const handleCancel = () => {
    setVisible(false);
    setEditingItem(null);
  };

  const columns = [
    {
      title: 'First Name',
      dataIndex: 'firstName',
      key: 'firstName',
    },
    {
      title: 'Last Name',
      dataIndex: 'lastName',
      key: 'lastName',
    },
    {
      title: 'Email',
      dataIndex: 'email',
      key: 'email',
    },
    {
      title: 'Phone',
      dataIndex: 'phone',
      key: 'phone',
    },
    {
      title: 'Address',
      dataIndex: 'address',
      key: 'address',
    },
    {
      title: 'Role',
      dataIndex: 'role',
      key: 'role',
      render: (role) => {
        switch (role) {
          case 'ADMIN':
            return 'Admin';
          case 'MANAGER':
            return 'Manager';
          case 'CONSULTING_STAFF':
            return 'Consulting Staff';
          case 'SALE_STAFF':
            return 'Sale Staff';
          case 'CUSTOMER':
            return 'Customer';
          default:
            return 'Unknown';
        }
      },
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
      <h1>Users Management</h1>
      <Button type="primary" onClick={() => handleEdit(null)}>
        Add User
      </Button>
      <Table dataSource={users} columns={columns} rowKey="id" />

      <Modal
        title={editingItem ? 'Edit User' : 'Add User'}
        open={visible}
        onOk={handleOk}
        onCancel={handleCancel}
      >
        <Form form={form} layout="vertical">
          <Form.Item
            name="firstName"
            label="First Name"
            rules={[{ required: true, message: 'Please enter first name' }]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            name="lastName"
            label="Last Name"
            rules={[{ required: true, message: 'Please enter last name' }]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            name="email"
            label="Email"
            rules={[{ required: true, message: 'Please enter email' }]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            name="role"
            label="Role"
            rules={[{ required: true, message: 'Please select a role' }]}
          >
            <Select placeholder="Select a role">
              <Option value="ADMIN">Admin</Option>
              <Option value="MANAGER">Manager</Option>
              <Option value="CUSTOMER">Customer</Option>
              <Option value="CONSULTING_STAFF">Consulting Staff</Option>
              <Option value="SALE_STAFF">Sale Staff</Option>
            </Select>
          </Form.Item>
          <Form.Item
            name="phone"
            label="Phone"
            rules={[
              { 
                required: true,
                message: 'Please enter phone number'
              },
              {
                pattern: /^\d{10}$/,
                message: 'Please enter a valid phone number (10 digits)'
              }]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            name="address"
            label="Address"
            rules={[{ required: true, message: 'Please enter address' }]}
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

export default UserList;
