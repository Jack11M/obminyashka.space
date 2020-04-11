import React from "react";
import { Link } from "react-router-dom";
import Avatar from "./Avatar.jsx";
import "./navtop.scss";

const NavTop = () => {
  return (
    <div className="navbar-top-inner">
      <div className="wrapper">
        <div className="navbar-top">
          <div className="navbar-top-links">
            <Link to="/" className="navbar-top-link">
              О Проекте
            </Link>
            <a href="#" className="navbar-top-link">
              <i className="icon-heart"></i>
              Доброе Дело
            </a>
          </div>
          <div id="personalArea">
            <Link to="/registration">
              <Avatar />
              <span>Мой кабинет</span>
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default NavTop;
