import React from 'react';
import { Redirect, Route, Switch } from 'react-router-dom';

import MyActivity from './my_activity';
import MyProfile from './my_profile/myProfile';
import MyFavorites from './my_favorites';
import MySettings from './my_settings';
import { route } from '../../../routes/routeConstants';


const RouterTabs = ( { url } ) => {

	return (
		<Switch>
			<Route path={ `${ url }` } component={ MyActivity } exact/>
			<Route path={ `${ url }${ route.myProfile }` } component={ MyProfile } exact/>
			<Route path={ `${ url }${ route.myFavorite }` } component={ MyFavorites } exact/>
			<Route path={ `${ url }${ route.mySettings }` } component={ MySettings } exact/>
			<Route path={ `${ url }${ route.exit }` } component={ MySettings } exact/>
			<Redirect to={ `${ url }` }/>
		</Switch>
	);
};

export default RouterTabs;
