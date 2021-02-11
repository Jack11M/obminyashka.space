import React from 'react';
import { Route, Switch } from 'react-router-dom';
import { useSelector } from 'react-redux';

import HomePage from '../pages/homepage/HomePage';
import UserInfo from '../pages/UserInfo/UserInfo';
import Auth from '../pages/FormRegister/Auth';
import Protected from './protectedRoute';
import FourOhFourPage from '../pages/ErrorPages/FourOhFourPage';
import { route } from './routeConstants';

export default () => {
	const { isAuthenticated } = useSelector( state => state.auth );

	return <Switch>
		<Route path={ route.home } component={ HomePage } exact/>
		<Protected
			path={ route.login }
			component={ Auth }
			permission={ !isAuthenticated }
			redirect={ route.home }/>
		<Protected
			path={ route.userInfo }
			component={ UserInfo }
			permission={ isAuthenticated }
			redirect={ route.home }/>
		<Route path={ route.noMatch } component={ FourOhFourPage }/>
	</Switch>;
};
