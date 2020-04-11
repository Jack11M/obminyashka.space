import React from "react";
import NavTop from "./nav_top/NavTop.jsx";
import NavMain from "./nav_main/NavMain.jsx";

import "./Navbar.scss";

const Header = () => {
  return (
    <header className="navbar">
      <NavTop />
      <NavMain />
    </header>
  );
};

export default Header;
