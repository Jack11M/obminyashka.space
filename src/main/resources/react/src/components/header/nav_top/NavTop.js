import React from 'react';
import { useSelector } from 'react-redux';
import { Link } from 'react-router-dom';

import Avatar from '../../avatar/avatar.js';
import SelectLang from '../../selectLang';
import { getTranslatedText } from '../../local/localisation';
import { route } from '../../../routes/routeConstants';

import './navtop.scss';
import { createProduct } from '../../../REST/Resources/fetchProfile';
import { toggleButtonLog } from '../../../redux/Utils';

// remove

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
							<i className="icon-heart"
							   onClick={()=> {
								createProduct(JSON.stringify({
										"age": "FROM_3_TO_5",
										"dealType": "EXCHANGE",
										"description": "Продам фліску з Кроликом в ідеалі!! Без дефектів і плям! Розмір вказаний 2/3 роки, ріст 92/98 см, можна одягати раніше! Є багато одягу, в оголошенні не все, запитуйте, скину фото)",
										"gender": "FEMALE",
										"id": 0,
										"images": [],
										"location": {
											"area": "string",
											"city": "string",
											"district": "string",
											"i18N": "EN",
											"id": 0
										},
										"readyForOffers": true,
										"season": "DEMI_SEASON",
										"size": "110см",
										"subcategoryId": 15,
										"topic": "Кофта детская с кроликом",
										"wishesToExchange": "ботинки зимние 250 мм на девочку, колготки 120см, шапочка для басейна, кофта для 7 лет, квартира в центре"
									})
								)
									.then(data => console.log('created: ', data))
							}}/>
							{getTranslatedText('header.goodness', lang)}
						</Link>
					</div>
					<div id="personalArea">
						<Link to={ isAuthenticated ? route.userInfo : route.login }>
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
