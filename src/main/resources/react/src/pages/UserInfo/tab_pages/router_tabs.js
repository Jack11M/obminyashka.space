import React, { useCallback, useEffect } from 'react';
import { Redirect, Route, Switch, useLocation } from 'react-router-dom';

import MyActivity from './my_activity';
import MyProfile from './my_profile';
import MyFavorites from './my_favorites';
import MySettings from './my_settings';
import { route } from '../../../routes/routeConstants';


const RouterTabs = ( { url, set } ) => {
	const location = useLocation();
	const [ prevLocation, setPrevLocation ] = set;


	useEffect( () => {
		return () => setPrevLocation( location.pathname );
	}, [ setPrevLocation, location ] );

	const findComponentForExit = useCallback( () => {
		let lastComponent = MyActivity;
		switch (prevLocation) {
			case route.userInfo:
				lastComponent = MyActivity;
				return MyActivity;
			case `${route.userInfo}${route.myProfile}`:
				lastComponent = MyProfile;
				return MyProfile;
			case `${route.userInfo}${route.myFavorite}`:
				lastComponent = MyFavorites;
				return MyFavorites;
			case `${route.userInfo}${route.mySettings}`:
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
