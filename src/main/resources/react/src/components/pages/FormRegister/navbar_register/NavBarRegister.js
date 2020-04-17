import React from "react";
import {NavLink} from "react-router-dom";
import cls from "./registerTabs.module.scss";

const NavBarRegister = () => {
	return (
		<div className={cls.tabs}>
			<NavLink to="/registration/" exact activeClassName={cls.active}>
				Вход
			</NavLink>
			<NavLink to="/registration/register" activeClassName={cls.active}>
				Регистрация
			</NavLink>
		</div>
	);
};
export default NavBarRegister;
