import React, { useEffect, useState } from "react";
import axios from "axios";
import { Table } from "antd";
import { toast } from "react-toastify";

function AdminStuff() {
  const api = "https://66fa57e7afc569e13a9b55ec.mockapi.io/User"; //api link
  const [createNewUserModel, setCreateNewUserModel] = useState(false);
  const [user, setUser] = useState([]);
  const [form] = useForm();
  const [submitting, setSubmitting] = useState(false); // dùng để khóa nút OK khi đang submit tránh người dùng bấm OK nhiều lần

  const fetchUser = async () => {
    try {
      const response = await axios.get(api);
      console.log(response.data);
      setUser(response.data);
      console.log("Success");
    } catch (ex) {
      toast.error("Get data failed");
      console.log("Failed");
    }
  };

  useEffect(() => {
    fetchUser();
  }, []);

  const columns = [
    {
      title: "ID",
      dataIndex: "id",
      key: "id",
    },
    {
      title: "User Name",
      dataIndex: "username",
      key: "username",
    },
    {
      title: "Email",
      dataIndex: "email",
      key: "email",
    },
    {
      title: "Address",
      dataIndex: "address",
      key: "address",
    },
    {
      title: "Phone Number",
      dataIndex: "phone",
      key: "phone",
    },
    {
      title: "Role",
      dataIndex: "role",
      key: "role",
    },
    {
      title: "Verifiled",
      dataIndex: "isVerifiled",
      key: "isVerifiled",
    },
    {
      title: "Action",
      dataIndex: "id",
      key: "id",
      render: (id) => {
        return (
          <>
            <Popconfirm
              title="Delete"
              description="Do you want to delete this account?"
              onConfirm={() => handleDeleteUser(id)}
            >
              <Button type="primary" danger>
                Delete
              </Button>
            </Popconfirm>
          </>
        );
      },
    },
  ];

  const handleOpenModal = () => {
    setCreateNewUserModel(true);
  };

  const handleOffModal = () => {
    setCreateNewUserModel(false);
  };

  const handleSubmitCreateAccount = async (user) => {
    try {
      setSubmitting(true);
      const response = await axios.post(api, user);
      toast.success("Create new account successfully");
      setCreateNewUserModel(false);
      form.resetFields();
      fetchUser();
    } catch (ex) {
      toast.error("Create new account failed");
    } finally {
      setSubmitting(false);
    }
  };

  const handleDeleteUser = async (id) => {
    try {
      await axios.delete(`${api}/${id}`);
      toast.success("Delete successfully");
      fetchUser();
    } catch (ex) {
      toast.error("Delete failed");
    }
  };

  return (
    <div>
      <h1>welcome admin: </h1>

      <Button type="primary" onClick={handleOpenModal}>
        Create new account
      </Button>

      <Table dataSource={user} columns={columns}></Table>

      <Modal
        confirmLoading={submitting}
        title="Create new account"
        open={createNewUserModel}
        onCancel={handleOffModal}
        onOk={() => form.submit()}
      >
        <Form onFinish={handleSubmitCreateAccount} form={form}>
          <Form.Item
            label="User's Name"
            name="username"
            rules={[
              {
                required: true,
                message: "Please enter User's Name",
              },
              {
                pattern: "^[a-zA-Z]+$",
                message: "only word in user's name",
              },
            ]}
          >
            <Input />
          </Form.Item>
          <FormItem
            label="Email"
            name="email"
            rules={[
              {
                required: true,
                message: "Please enter your email",
              },
              {
                pattern: "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}$",
                message: "Invalid email",
              },
            ]}
          >
            <Input />
          </FormItem>
          <FormItem
            label="Address"
            name="address"
            rules={[
              {
                required: true,
                message: "Please enter your address",
              },
            ]}
          >
            <Input />
          </FormItem>
          <FormItem
            label="Phone"
            name="phone"
            rules={[
              {
                required: true,
                message: "Please enter your phone number",
              },
              {
                pattern: "^[0-9]{9,11}$",
                message: "Invalid phone number",
              },
            ]}
          >
            <Input />
          </FormItem>
        </Form>
      </Modal>
    </div>
  );
}

export default AdminStuff;
