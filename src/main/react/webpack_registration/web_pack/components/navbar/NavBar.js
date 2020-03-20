import React from "react";
import { NavLink } from "react-router-dom";
import "./tabs.scss";

const NavBar = () => {
  return (
    <div className="tabs">
      <NavLink to="/registration" exact>
        Вход
      </NavLink>
      <NavLink to="/registration/register">Регистрация</NavLink>
    </div>
  );
};
export default NavBar;
