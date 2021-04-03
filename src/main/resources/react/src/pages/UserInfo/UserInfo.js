import React, { useCallback, useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useRouteMatch } from 'react-router-dom';

import Tabs from './tabs';
import RouterTabs from './tab_pages/router_tabs';
import ActiveProfile from './active_profile';
import Exit from './tab_pages/exit/index'
import { fetchUserInfoAsync } from '../../redux/profile/profileAction';

import './UserInfo.scss';

const UserInfo = () => {
	const dispatch = useDispatch();
	let {  url } = useRouteMatch();
	const [isModalOpen, setIsModalOpen] = useState(false)
	const { profile } = useSelector( ( state ) => state.profileMe );
	const { firstName, lastName, avatarImage } = profile;

	useEffect( () => {
		dispatch( fetchUserInfoAsync() );
	}, [ dispatch ] );


	const toggle = useCallback(() => {
		setIsModalOpen(state => !state);
	},[])

	return (
		<div className="container">
			<aside className="left-side">
				<ActiveProfile
					firstName={ firstName }
					lastName={ lastName }
					avatar={ avatarImage }
				/>
				<Tabs url={ url } toggle={toggle}/>
			</aside>
			<main className="main-content">
				<div className="main-content-wrapper">
					<RouterTabs url={ url } profile={ profile }/>
				</div>
			</main>
			{isModalOpen && <Exit toggle={toggle}/>}
		</div>
	);
};

export default UserInfo;
