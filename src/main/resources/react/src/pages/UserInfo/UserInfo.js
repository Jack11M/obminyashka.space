import React, { useCallback, useState } from 'react';
import { useSelector } from 'react-redux';
import { useHistory, useRouteMatch } from 'react-router-dom';

import Tabs from './tabs';
import RouterTabs from './tab_pages/router_tabs';
import ActiveProfile from './active_profile';
import Exit from './tab_pages/exit/index';

import './UserInfo.scss';

const UserInfo = () => {
	let { url } = useRouteMatch();
	let history = useHistory();
	const [ isModalOpen, setIsModalOpen ] = useState( false );
	const [ prevLocation, setPrevLocation ] = useState( '' );

	const { firstName, lastName, avatarImage } = useSelector( ( state ) => state.profileMe );


	const open = useCallback( () => {
		setIsModalOpen( true );
	}, [] );

	const close = useCallback( () => {
		setIsModalOpen( false );
		history.push( prevLocation );

	}, [ prevLocation, history ] );

	return (
		<div className="container">
			<aside className="left-side">
				<ActiveProfile
					firstName={ firstName }
					lastName={ lastName }
					avatar={ avatarImage }
				/>
				<Tabs url={ url } toggle={ open }/>
			</aside>
			<main className="main-content">
				<div className="main-content-wrapper">
					<RouterTabs url={ url } set={ [ prevLocation, setPrevLocation ] }/>
				</div>
			</main>
			{ isModalOpen && <Exit toggle={ close }/> }
		</div>
	);
};

export default UserInfo;
