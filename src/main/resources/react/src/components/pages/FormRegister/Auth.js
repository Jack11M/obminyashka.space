import React from "react";
import NavBarRegister from "./navbar_register/NavBarRegister.js";
import RoutesRegister from "./routesRegister.js";
import cls from "./Auth.module.scss";

const Auth = () => {
	return (
		<div className={cls.popup}>
			<NavBarRegister/>
			<RoutesRegister/>
		</div>
	);
};

export default Auth;
