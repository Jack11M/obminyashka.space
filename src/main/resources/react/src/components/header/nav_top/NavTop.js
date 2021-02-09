import React from 'react';
import { Link } from 'react-router-dom';
import Avatar from '../../avatar/avatar.js';
import SelectLang from '../../selectLang';
import { getTranslatedText } from '../../local/localisation';

import './navtop.scss';
import { useSelector } from 'react-redux';

const NavTop = () => {
	const {lang, isAuthenticated, username, usernameOrEmail} = useSelector(state => state.auth);

	const myName = username|| usernameOrEmail

	return (
		<div className="navbar-top-inner">
			<div className="wrapper">
				<div className="navbar-top">
					<div className="navbar-top-links">
						<Link to="/" className="navbar-top-link">
							{ getTranslatedText('header.about', lang)}
						</Link>
						<Link to="/" className="navbar-top-link">
							<i className="icon-heart"/>
							{getTranslatedText('header.goodness', lang)}
						</Link>
					</div>
					<div id="personalArea">
						<Link to={ isAuthenticated ? '/userInfo/activity':'/login' }>
							<Avatar whatIsClass={ 'user-photo' } width={ 30 } height={ 28 }/>
							<span>{myName || getTranslatedText('header.myOffice', lang)}</span>
						</Link>
						<SelectLang/>
					</div>
				</div>
			</div>
		</div>
	);
};

export default NavTop;
