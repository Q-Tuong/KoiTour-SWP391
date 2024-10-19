import React, { Fragment } from "react";
import Header from "../Header/Header";
import Routers from "../../Routers/Routers";
import Footer from "../Footer/Footer";

const Layout = () => {
  return (
    <Fragment>
      <Header />
      <Routers />
      <Footer />
    </Fragment>
  );
};

export default Layout;
