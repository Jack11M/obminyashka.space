import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useLocation } from 'react-router-dom';

import { getTranslatedText } from '../../components/local/localisation';
import CheckBox from '../../components/checkbox';
import Button from '../../components/button/Button';
import InputForAuth from './InputForAuth';
import { clearValueLogin, inputChangeAuth, toggleCheckBox } from '../../redux/auth/action';

import { Extra, ExtraLink } from './loginStyle';

const LogIn = () => {
	const location = useLocation();
	const dispatch = useDispatch();
	const { lang, logEmail, logPassword, logCheckbox } = useSelector( state => state.auth );

	useEffect( () => {
		 dispatch( clearValueLogin() );
	}, [dispatch, location.pathname] );

	const changeInput = ( e ) => {
		dispatch( inputChangeAuth( [ e.target.name, e.target.value ] ) );
	};

	const changeCheckBox = () => {
		dispatch( toggleCheckBox( { logCheckbox: !logCheckbox } ) );
	};

	return (
		<form>
			<div>
				<InputForAuth
					text={ getTranslatedText( 'auth.logEmail', lang ) }
					name={ 'logEmail' }
					type={ 'text' }
					value={ logEmail.value }
					error={ logEmail.error }
					click={ changeInput }
				/>
				<InputForAuth
					text={ getTranslatedText( 'auth.logPassword', lang ) }
					name={ 'logPassword' }
					type={ 'password' }
					value={ logPassword.value }
					error={ logPassword.error }
					click={ changeInput }
				/>
			</div>
			<Extra>
				<CheckBox
					text={ getTranslatedText( 'auth.remember', lang ) }
					margin={ '0 0 44px 0' }
					fs={ '14px' }
					checked={ logCheckbox }
					click={ changeCheckBox }
				/>
				<ExtraLink to={ '/logIn/signUp' }>
					{ getTranslatedText( 'auth.noLogin', lang ) }
				</ExtraLink>
			</Extra>
			<Button
				text={ getTranslatedText( 'button.enter', lang ) }
				disabling={ false }
				mb={ '64px' }
				bold
				lHeight={ '24px' }
				width={ '222px' }
				height={ '48px' }/>
		</form>
	);
};

export default LogIn;