import {Route, Switch} from "react-router-dom";
import React from "react";
import HomePage from "../components/pages/homepage/HomePage.js";
import Auth from "../components/pages/FormRegister/Auth.js";

export default () => {
	return (
		<Switch>
			<Route path="/" component={HomePage} exact/>
			<Route path="/registration/" component={Auth}/>
			<Route path="/registration/register" exact component={Auth}/>
		</Switch>
	);
};
