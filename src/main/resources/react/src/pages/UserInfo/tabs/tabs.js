import React from 'react';
import { NavLink } from 'react-router-dom';
import { useSelector } from 'react-redux';

import { route } from '../../../routes/routeConstants';
import { getTranslatedText } from '../../../components/local/localisation';

import './tabs.scss';

const createNavLink = ( { url, classname, textKey, lang, click = null, exact = false } ) => {
	return (
		<NavLink to={ url } exact={ exact } onClick={ click }>
			<i className={ classname }/>
			{ getTranslatedText( textKey, lang ) }
			<i className="active__cycle"/>
		</NavLink>);
};

const Tabs = props => {
	const { url, toggle } = props;
	const { lang } = useSelector( state => state.auth );

	return (
		<div className="tabs">
			{ createNavLink( { url, classname: 'icon-activity', textKey: 'panel.myActivity', lang, exact: true } ) }
			{ createNavLink( { url: `${ url }${ route.myProfile }`, classname: 'icon-profile', textKey: 'panel.myProfile', lang } ) }
			{ createNavLink( { url: `${ url }${ route.myFavorite }`, classname: 'icon-star', textKey: 'panel.myFavorite', lang } ) }
			{ createNavLink( { url: `${ url }${ route.mySettings }`, classname: 'icon-settings', textKey: 'panel.mySettings', lang } ) }
			{ createNavLink( { url: `${ url }${ route.exit }`, classname: 'icon-logout', textKey: 'panel.myExit', lang, click: toggle } ) }
		</div>
	);
};

export default Tabs;
