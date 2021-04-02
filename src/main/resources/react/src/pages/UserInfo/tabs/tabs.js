import React from 'react';
import { NavLink } from 'react-router-dom';
import { useSelector } from 'react-redux';

import { route } from '../../../routes/routeConstants';
import { getTranslatedText } from '../../../components/local/localisation';

import './tabs.scss';

const createNavLink = ( url, classname, textKey, lang, exact = false ) => {
	return (<NavLink to={ url } exact={ exact }>
		<i className={ classname }/>
		{ getTranslatedText( textKey, lang ) }
		<i className="active__cycle"/>
	</NavLink>);
};

const Tabs = props => {
	const { url } = props;
	const { lang } = useSelector( state => state.auth );
	return (
		<div className="tabs">
			{ createNavLink( url, 'icon-activity', 'ownInfo.panelMyActivity', lang, true ) }
			{ createNavLink( `${ url }${ route.myProfile }`, 'icon-profile', 'ownInfo.panelMyProfile', lang ) }
			{ createNavLink( `${ url }${ route.myFavorite }`, 'icon-star', 'ownInfo.panelMyFavorite', lang ) }
			{ createNavLink( `${ url }${ route.mySettings }`, 'icon-settings', 'ownInfo.panelMySettings', lang ) }
			{ createNavLink( `${ url }${ route.exit }`, 'icon-logout', 'ownInfo.panelExit', lang ) }
		</div>
	);
};

export default Tabs;
