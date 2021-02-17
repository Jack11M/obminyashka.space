import React from 'react';
import { Route, Switch, Redirect } from 'react-router-dom';
import MyActivity from './my_activity';
import MyProfile from './my_profile/myProfile';
import MyFavorites from './my_favorites';
import MySettings from './my_settings';
import { route } from '../../../routes/routeConstants';


const RouterTabs = ( props ) => {
	const { path } = props;
	return (
		<Switch>
			<Route path={ `${ path }${route.activity}` } component={ MyActivity } exact/>
			<Route path={ `${ path }${route.myProfile}` } component={ MyProfile }/>
			<Route path={ `${ path }${route.myFavorite}` } component={ MyFavorites }/>
			<Route path={ `${ path }${route.mySettings}` } component={ MySettings }/>
			<Route path={ `${ path }${route.exit}` } component={ MySettings }/>
			<Redirect to={`${ path }${route.activity}`} />

		</Switch>
	);
};

export default RouterTabs;
