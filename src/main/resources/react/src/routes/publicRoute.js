import React from 'react';
import { Route, Switch } from 'react-router-dom';

import HomePage from '../pages/homepage/HomePage';
import Auth from '../pages/FormRegister/Auth';
import FourOhFourPage from '../pages/ErrorPages/FourOhFourPage';
import { route } from './routeConstants';
import ProductPage from '../pages/ProductPage/ProductPage';


const Public = () => {
	return (
		<Switch>
			<Route path={route.home} component={ HomePage } exact/>
			<Route path={route.login} component={ Auth }/>
			<Route path={route.productPage} component={ ProductPage }/>
			<Route path={route.noMatch} component={ FourOhFourPage }/>
		</Switch>
	);
};

export default Public;
