import React from 'react';
import { Route, Switch } from 'react-router-dom';
import HomePage from '../pages/homepage/HomePage';
import UserInfo from '../pages/UserInfo/UserInfo';
import FourOhFourPage from '../pages/ErrorPages/FourOhFourPage';

const Protected = () => {
	return (
		<Switch>
			<Route path="/" component={ HomePage } exact/>
			<Route path="/userInfo/" component={ UserInfo }/>
			<Route path="*" component={ FourOhFourPage }/>
		</Switch>
	);
};

export default Protected;
