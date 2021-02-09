import React from 'react';
import { Route, Switch } from 'react-router-dom';
import { useSelector } from 'react-redux';

import HomePage from '../pages/homepage/HomePage.js';
import Auth from '../pages/FormRegister/Auth.js';
import UserInfo from '../pages/UserInfo/UserInfo';
import FourOhFourPage from '../pages/ErrorPages/FourOhFourPage';

export default () => {
	const { isAuthenticated } = useSelector( state => state.auth );

	return isAuthenticated ? (
		<Switch>
			<Route path="/" component={ HomePage } exact/>
			<Route path="/userInfo/" component={ UserInfo }/>
			<Route path="*" component={ FourOhFourPage }/>
		</Switch>
	) : (
		<Switch>
			<Route path="/" component={ HomePage } exact/>
			<Route path="/login/" component={ Auth }/>
			<Route path="*" component={ FourOhFourPage }/>
		</Switch>
	);
};
