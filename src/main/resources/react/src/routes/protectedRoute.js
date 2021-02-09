import React from 'react';
import { Route, Switch } from 'react-router-dom';

import HomePage from '../pages/homepage/HomePage';
import UserInfo from '../pages/UserInfo/UserInfo';
import FourOhFourPage from '../pages/ErrorPages/FourOhFourPage';
import { route } from './routeConstants';

const Protected = () => {
	return (
		<Switch>
			<Route path={ route.home } component={ HomePage } exact/>
			<Route path={route.userInfo} component={ UserInfo }/>
			<Route path={route.noMatch} component={ FourOhFourPage }/>
		</Switch>
	);
};

export default Protected;
