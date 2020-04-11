import React from "react";
import "./navMain.scss";
import { Link } from "react-router-dom";
import ButtonAdv from "../../../assets/buttonAdv/ButtonAdv.jsx";
import NavCategory from "../nav_category/NavCategory.jsx";

const NavMain = () => {
  return (
    <div className="navbar-main-inner">
      <div className="wrapper">
        <div className="navbar-main">
          <Link to="/" className="logo"></Link>
          <div className="navbar-main__select">
            <div className={"navbar-main__select-top"}>Категории</div>
            <div className={"navbar-main__select-bottom"}>Категории</div>
            <NavCategory />
          </div>
          <div className="navbar-main-search">
            <input type="text" id="search" placeholder="Я ищу..." />
            <label htmlFor="search" className="circle">
              <label htmlFor="search" className="icon-search"></label>
            </label>
          </div>
          <ButtonAdv />
        </div>
      </div>
    </div>
  );
};

export default NavMain;
