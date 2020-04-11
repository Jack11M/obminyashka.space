import React from "react";
import { BrowserRouter as Router } from "react-router-dom";
import NavBarRegister from "./navbar_register/NavBarRegister.jsx";
import RoutesRegister from "./routesRegister.jsx";
import cls from "./Auth.module.scss";

const Auth = () => {
  return (
    <div className={cls.popup}>
      <NavBarRegister />
      <RoutesRegister />
    </div>
  );
};

export default Auth;
