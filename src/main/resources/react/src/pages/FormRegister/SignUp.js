import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useLocation } from 'react-router-dom';

import CheckBox from '../../components/checkbox';
import Button from '../../components/button/Button';
import InputForAuth from './InputForAuth';
import { getTranslatedText } from '../../components/local/localisation';
import { clearValueSignUp, inputChangeAuth, toggleCheckBox } from '../../redux/auth/action';

import { Extra } from './loginStyle';


const SignUp = () => {
	const location = useLocation();
	const dispatch = useDispatch();
	const { lang, regEmail, regNick, regPassword, regConfirm, regCheckbox } = useSelector( state => state.auth );

	useEffect( () => {
	 dispatch( clearValueSignUp() );
	}, [dispatch, location.pathname] );

	const changeRegInput = ( e ) => {
		dispatch( inputChangeAuth( [ e.target.name, e.target.value ] ) );
	};

	const changeCheckBox = () => {
		dispatch( toggleCheckBox( { regCheckbox: !regCheckbox } ) );
	};


	return (
		<form>
			<div>
				<InputForAuth
					text={ getTranslatedText( 'auth.regEmail', lang ) }
					name={ 'regEmail' }
					type={ 'text' }
					value={ regEmail.value }
					error={ regEmail.error }
					click={ changeRegInput }
				/>
				<InputForAuth
					text={ getTranslatedText( 'auth.regLogin', lang ) }
					name={ 'regNick' }
					type={ 'text' }
					value={ regNick.value }
					error={ regNick.error }
					click={ changeRegInput }
				/>
				<InputForAuth
					text={ getTranslatedText( 'auth.regPassword', lang ) }
					name={ 'regPassword' }
					type={ 'password' }
					value={ regPassword.value }
					error={ regPassword.error }
					click={ changeRegInput }
				/>
				<InputForAuth
					text={ getTranslatedText( 'auth.regConfirm', lang ) }
					name={ 'regConfirm' }
					type={ 'password' }
					value={ regConfirm.value }
					error={ regConfirm.error }
					click={ changeRegInput }
				/>
			</div>
			<Extra>
				<CheckBox
					text={ getTranslatedText( 'auth.agreement', lang ) }
					margin={ '0 0 44px 0' }
					fs={ '14px' }
					checked={ regCheckbox }
					click={ changeCheckBox }
				/>
			</Extra>
			<Button
				text={ getTranslatedText( 'auth.signUp', lang ) }
				disabling={ false }
				mb={ '44px' }
				bold
				lHeight={ '24px' }
				width={ '222px' }
				height={ '48px' }
			/>
		</form>
	);
};

export default SignUp;