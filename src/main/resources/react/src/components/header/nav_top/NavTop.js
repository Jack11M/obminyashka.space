import React from "react";
import { Link } from "react-router-dom";
import Avatar from "../../avatar/avatar.js";
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
            <Link to="/" className="navbar-top-link">
              <i className="icon-heart" />
              Доброе Дело
            </Link>
          </div>
          <div id="personalArea">
            <Link to="/userInfo">
              <Avatar whatIsClass={'user-photo'} width={30} height={28}/>
              <span>Мой кабинет</span>
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default NavTop;
