import React, { useCallback, useEffect, useState } from 'react';
import { Redirect, Route, Switch, useLocation } from 'react-router-dom';

import MyActivity from './my_activity';
import MyProfile from './my_profile/myProfile';
import MyFavorites from './my_favorites';
import MySettings from './my_settings';
import { route } from '../../../routes/routeConstants';


const RouterTabs = ( { url } ) => {
	const location = useLocation();
	const [ prevLocation, setPrevLocation ] = useState( '' );

	useEffect( () => {
		return () => setPrevLocation( location.pathname );
	}, [ location ] );

	const findComponentForExit = useCallback( () => {
		let lastComponent = MyActivity;
		switch (prevLocation) {
			case '/user':
				lastComponent = MyActivity;
				return MyActivity;
			case '/user/profile':
				lastComponent = MyProfile;
				return MyProfile;
			case '/user/favorites':
				lastComponent = MyFavorites;
				return MyFavorites;
			case '/user/settings':
				lastComponent = MySettings;
				return MySettings;
			default:
				return lastComponent;
		}
	}, [ prevLocation ] );

	return (
		<Switch>
			<Route path={ `${ url }` } component={ MyActivity } exact/>
			<Route path={ `${ url }${ route.myProfile }` } component={ MyProfile }/>
			<Route path={ `${ url }${ route.myFavorite }` } component={ MyFavorites }/>
			<Route path={ `${ url }${ route.mySettings }` } component={ MySettings }/>
			<Route path={ `${ url }${ route.exit }` } component={ findComponentForExit() }/>
			<Redirect to={ `${ url }` }/>
		</Switch>
	);
};

export default RouterTabs;
