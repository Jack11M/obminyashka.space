import React from "react";
import { NavLink } from "react-router-dom";
import {getTranslatedText} from '../../../components/local/localisation';
import cls from "./registerTabs.module.scss";

const NavBarRegister = () => {
  return (
    <div className={cls.tabs}>
      <NavLink to="/logIn/" exact activeClassName={cls.active}>
        { getTranslatedText('auth.logIn') }
      </NavLink>
      <NavLink to="/logIn/signUp" activeClassName={cls.active}>
        { getTranslatedText('auth.signUp') }
      </NavLink>
    </div>
  );
};
export default NavBarRegister;
