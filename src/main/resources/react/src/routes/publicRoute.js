import React from 'react';
import { Route, Switch } from 'react-router-dom';
import HomePage from '../pages/homepage/HomePage';
import Auth from '../pages/FormRegister/Auth';
import FourOhFourPage from '../pages/ErrorPages/FourOhFourPage';

const Public = () => {
	return (
		<Switch>
			<Route path="/" component={ HomePage } exact/>
			<Route path="/login" component={ Auth }/>
			<Route path="*" component={ FourOhFourPage }/>
		</Switch>
	);
};

export default Public;
