import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useRouteMatch } from 'react-router-dom';

import Tabs from './tabs';
import RouterTabs from './tab_pages/router_tabs';
import ActiveProfile from './active_profile';
import { fetchUserInfoAsync } from '../../redux/profile/profileAction';

import './UserInfo.scss';

const UserInfo = () => {
	const dispatch = useDispatch();
	let {  url } = useRouteMatch();
	const { profile } = useSelector( ( state ) => state.profileMe );

	useEffect( () => {
		dispatch( fetchUserInfoAsync() );
	}, [ dispatch ] );

	const { firstName, lastName, avatarImage } = profile;

	return (
		<div className="container">
			<aside className="left-side">
				<ActiveProfile
					firstName={ firstName }
					lastName={ lastName }
					avatar={ avatarImage }
				/>
				<Tabs url={ url }/>
			</aside>
			<main className="main-content">
				<div className="main-content-wrapper">
					<RouterTabs url={ url } profile={ profile }/>
				</div>
			</main>
		</div>
	);
};

export default UserInfo;
