import React from 'react';
import { useSelector } from 'react-redux';
import { NavLink } from 'react-router-dom';
import { getTranslatedText } from '../../../components/local/localisation';
import cls from './registerTabs.module.scss';

const NavBarRegister = () => {
	const { lang } = useSelector( state => state.auth );
	return (
		<div className={ cls.tabs }>
			<NavLink to="/logIn/" exact activeClassName={ cls.active }>
				{ getTranslatedText( 'auth.logIn', lang ) }
			</NavLink>
			<NavLink to="/logIn/signUp" activeClassName={ cls.active }>
				{ getTranslatedText( 'auth.signUp', lang ) }
			</NavLink>
		</div>
	);
};
export default NavBarRegister;
