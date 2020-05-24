import React from "react";
import NavTop from "./nav_top/NavTop.js";
import NavMain from "./nav_main/NavMain.js";

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
