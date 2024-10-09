import React, { useEffect, useState } from "react";
import axios from "axios";
import { Table } from "antd";
import { toast } from "react-toastify";

function AdminStuff() {
  const [user, setUser] = useState([]);
  const api = "https://66fa57e7afc569e13a9b55ec.mockapi.io/User"; //api link

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

  const handleCreateAccount = async () => {};

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
      <Button type="primary" onClick={handleCreateAccount}>
        Create new account
      </Button>
      <Table dataSource={user} columns={columns}></Table>
    </div>
  );
}

export default AdminStuff;
