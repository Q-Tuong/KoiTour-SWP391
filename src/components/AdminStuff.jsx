import {
  Button,
  Form,
  Image,
  Input,
  InputNumber,
  Modal,
  Popconfirm,
  Table,
  Upload,
} from "antd";
import { useForm } from "antd/es/form/Form";
import FormItem from "antd/es/form/FormItem";
import axios from "axios";
import { PlusOutlined } from "@ant-design/icons";
import React, { useEffect, useState } from "react";
import { toast } from "react-toastify";
import uploadFile from "./utils/file";

// gõ "rfce" để tự động generate (sau khi đã cài ES7)

function StudentManagement() {
  const api = "https://66fa57e7afc569e13a9b55ec.mockapi.io/User"; //api link
  // http://localhost:8080/api/user/get-all (link mockAPI)
  const [createNewUserModel, setCreateNewUserModel] = useState(false);
  const [user, setUser] = useState([]);
  const [form] = useForm();
  const [submitting, setSubmitting] = useState(false); // dùng để khóa nút OK khi đang submit tránh người dùng bấm OK nhiều lần
  const [updateModal, setUpdateModal] = useState(false);
  const [updating, setUpdating] = useState(false);
  const [userData, setUserData] = useState({
    username: "",
    email: "",
    address: "",
    phone: "",
    role: "",
  });

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
    // {
    //   title: "Verifiled",
    //   dataIndex: "isVerifiled",
    //   key: "isVerifiled",
    // },
    {
      title: "Action",
      dataIndex: "id",
      key: "id",
      render: (id, role) => {
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

            <Button type="primary" onClick={() => handleOpenUpdateModal(role)}>
              Update
            </Button>
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

  const handleOpenUpdateModal = (user) => {
    setUserData(user); // Thiết lập userData cho người dùng cần cập nhật
    form.setFieldsValue(user); // Điền dữ liệu vào form
    setUpdateModal(true); // Mở modal cập nhật
  };

  const handleOffUpdateModal = () => {
    setUpdateModal(false);
  };

  const handleSubmitCreateAccount = async (user) => {
    try {
      setSubmitting(true);
      const response = await axios.post(`${api}/${userData.id}`, user);
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

  const handleUpdateUser = async (values) => {
    setSubmitting(true);
    try {
      const response = await axios.put(`${api}/${userData.id}`, values);
      setUserData(response.data);
      form.setFieldsValue(response.data);
      toast.success("Update Success fully");
      setUpdateModal(false);
      fetchUser();
    } catch (ex) {
      toast.error("Update failed");
      console.log(ex);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div>
      <h1>welcome admin</h1>

      <Button type="primary" onClick={handleOpenModal}>
        Create new account
      </Button>

      <Table dataSource={user} columns={columns}></Table>

      {/* Modal create new account */}
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
          <FormItem label="Role" name="role">
            <select>
              <option value="User">User</option>
              <option value="Admin">Admin</option>
            </select>
          </FormItem>
        </Form>
      </Modal>

      {/* Modal update account */}
      <Modal
        title="Update role"
        confirmLoading={submitting}
        open={updateModal}
        onCancel={handleOffUpdateModal}
        onOk={() => form.submit()}
      >
        <Form onFinish={handleUpdateUser} form={form}>
          <FormItem label="Role" name="role">
            <select>
              <option value="User">User</option>
              <option value="Admin">Admin</option>
            </select>
          </FormItem>
        </Form>
      </Modal>
    </div>
  );
}

export default StudentManagement;
