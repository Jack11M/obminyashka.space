import React from 'react';
import { Redirect, Route, Switch } from 'react-router-dom';
import { useSelector } from 'react-redux';

import HomePage from '../pages/homepage/HomePage.js';
import Auth from '../pages/FormRegister/Auth.js';
import UserInfo from '../pages/UserInfo/UserInfo';
import ProductPage from '../pages/ProductPage/ProductPage';

export default () => {
	const { isAuthenticated } = useSelector( state => state.auth );

	return isAuthenticated ? (
		<Switch>
			<Route path="/" component={ HomePage } exact/>
			<Route path="/userInfo/" component={ UserInfo }/>
			<Redirect to={ '/' }/>
		</Switch>
	) : (
		<Switch>
			<Route path="/" component={ HomePage } exact/>
			<Route path="/login/" component={ Auth }/>
			<Route path="/product_page" component={ ProductPage }/>
			<Redirect to={ '/login/' }/>
		</Switch>
	);
};
