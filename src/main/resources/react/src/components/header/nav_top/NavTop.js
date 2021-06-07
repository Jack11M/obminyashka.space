import React from 'react';
import { useSelector } from 'react-redux';
import { Link } from 'react-router-dom';

import Avatar from '../../common/avatar/avatar.js';
import SelectLang from '../../selectLang';
import { getTranslatedText } from '../../local/localisation';
import { route } from '../../../routes/routeConstants';

import './navtop.scss';

const NavTop = () => {
	const { lang } = useSelector( state => state.auth );
	const { isAuthenticated, username } = useSelector( state => state.auth );



	return (
		<div className="navbar-top-inner">
			<div className="wrapper">
				<div className="navbar-top">
					<div className="navbar-top-links">
						<Link to="/" className="navbar-top-link">
							{ getTranslatedText( 'header.about', lang ) }
						</Link>
						<Link to={ route.home } className="navbar-top-link">
							<i className="icon-heart"/>
							{ getTranslatedText( 'header.goodness', lang ) }
						</Link>
					</div>
					<div id="personalArea">
						<Link to={ isAuthenticated ? route.userInfo : route.login }>
							<Avatar whatIsClass={ 'user-photo' } width={ 30 } height={ 28 }/>
							<span>{ username || getTranslatedText( 'header.myOffice', lang ) }</span>
						</Link>
						<SelectLang/>
					</div>
				</div>
			</div>
		</div>
	);
};

export default NavTop;
