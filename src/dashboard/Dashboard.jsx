import { useState } from "react";
import {
  BarChartOutlined,
  GithubOutlined,
  HomeFilled,
  UserOutlined,
} from "@ant-design/icons";
import { Breadcrumb, Layout, Menu, Avatar, Input, Dropdown } from "antd";
import { Link, Outlet } from "react-router-dom";
import "./Dashboard.css";

const { Header, Content, Footer, Sider } = Layout;
const { Search } = Input;

function getItem(label, key, icon, children) {
  return {
    key,
    icon,
    children,
    label: <Link to={`/dashboard/${key}`}>{label}</Link>,
  };
}

const items = [
  getItem("Manage Breed", "breed", <GithubOutlined />),
  getItem("Manage koi-fish", "koi-fish", <GithubOutlined />),
  getItem("User Management", "users", <UserOutlined />),
  getItem("Reports", "reports", <BarChartOutlined />, [
    getItem("Monthly", "reports/monthly"),
    getItem("Yearly", "reports/yearly"),
  ]),
];


const Dashboard = () => {
  const [collapsed, setCollapsed] = useState(false);

  return (
    <Layout className="dashboard-layout">
      <Sider
        collapsible
        collapsed={collapsed}
        onCollapse={(value) => setCollapsed(value)}
        className="dashboard-sider"
      >
        <div className="logo-container">
          <img src="/image/logo.svg" alt="Koi Fish" className="logo" />
        </div>
        <Menu
          theme="dark"
          defaultSelectedKeys={["1"]}
          mode="inline"
          items={items}
        />
      </Sider>
      <Layout>
        <Header className="dashboard-header">
          <div className="header-content">
            <div className="header-title">Dashboard Management</div>
            <div className="search-container"></div>

            <Dropdown
              overlay={
                <Menu>
                  <Menu.Item key="1">
                    <Link to="/profile">Profile</Link>
                  </Menu.Item>
                  <Menu.Item key="2">
                    <Link to="/settings">Settings</Link>
                  </Menu.Item>
                  <Menu.Item key="3">Logout</Menu.Item>
                </Menu>
              }
            >

              <Avatar
                size="large"
                icon={<UserOutlined />}
                className="header-avatar"
              />
            </Dropdown>

          </div>
        </Header>

        <Content className="dashboard-content">
          <div className="content-header">
            <Breadcrumb className="dashboard-breadcrumb">
              <Breadcrumb.Item>
                <Link to="/">
                  <HomeFilled />
                </Link>
              </Breadcrumb.Item>
              <Breadcrumb.Item>Dashboard</Breadcrumb.Item>
            </Breadcrumb>
            <Search
              placeholder="Search..."
              onSearch={(value) => console.log(value)}
              className="search-bar"
              style={{ maxWidth: 300 }}
            />
          </div>
          <div className="dashboard-inner-content">
            <Outlet />
          </div>
        </Content>
        <Footer className="dashboard-footer">
          Koi Fish Dashboard ©{new Date().getFullYear()} Created by
          KoiFIshTour99 Team
        </Footer>
      </Layout>
    </Layout>
  );
};

export default Dashboard;
