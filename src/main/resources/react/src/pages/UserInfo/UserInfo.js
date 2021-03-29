import React from 'react';
import { useSelector } from 'react-redux';
import { useRouteMatch } from 'react-router-dom';

import Tabs from './tabs';
import RouterTabs from './tab_pages/router_tabs';
import ActiveProfile from './active_profile';

import './UserInfo.scss';

const UserInfo = () => {
	let { url } = useRouteMatch();
	const { firstName, lastName, avatarImage } = useSelector( ( state ) => state.profileMe );


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
					<RouterTabs url={ url }/>
				</div>
			</main>
		</div>
	);
};

export default UserInfo;
