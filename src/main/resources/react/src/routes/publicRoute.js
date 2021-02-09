import React from 'react';
import { Route, Switch } from 'react-router-dom';

import HomePage from '../pages/homepage/HomePage';
import Auth from '../pages/FormRegister/Auth';
import FourOhFourPage from '../pages/ErrorPages/FourOhFourPage';
import { route } from './routeConstants';


const Public = () => {
	return (
		<Switch>
			<Route path={route.home} component={ HomePage } exact/>
			<Route path={route.login} component={ Auth }/>
			<Route path={route.noMatch} component={ FourOhFourPage }/>
		</Switch>
	);
};

export default Public;
