import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useHistory, useLocation } from 'react-router-dom';

import CheckBox from '../../components/checkbox';
import Button from '../../components/button/Button';
import InputForAuth from './InputForAuth';
import { getTranslatedText } from '../../components/local/localisation';
import {
	clearValueLogin,
	clearValueSignUp,
	inputChangeAuth,
	postAuthRegisterAsync,
	toggleCheckBox,
	toggleDisableButtonReg
} from '../../redux/auth/action';
import SpinnerForAuthBtn from '../../components/spinner/spinnerForAuthBtn';

import { Extra } from './loginStyle';


const SignUp = () => {
	const history = useHistory();
	const location = useLocation();
	const dispatch = useDispatch();
	const { lang, regEmail, regNick, regPassword, regConfirm, regCheckbox, disableReg, successRegister } = useSelector( state => state.auth );
	const { isFetching } = useSelector( state => state.ui );

	useEffect( () => {
		if (successRegister) history.push( '/logIn/' );
	}, [ history, successRegister ] );

	useEffect( () => {
		dispatch( clearValueSignUp() );
		dispatch( clearValueLogin() );
	}, [ dispatch, location.pathname ] );

	useEffect( () => {
		dispatch( toggleDisableButtonReg() );
	}, [ dispatch, regEmail, regNick, regPassword, regConfirm, regCheckbox ] );

	useEffect( () => {
		dispatch( inputChangeAuth( [ 'regConfirm', regConfirm.value ] ) );
	}, [ dispatch, regPassword, regConfirm.value ] );

	const changeRegInput = ( e ) => {
		dispatch( inputChangeAuth( [ e.target.name, e.target.value ] ) );
	};

	const changeCheckBox = () => {
		dispatch( toggleCheckBox( { regCheckbox: !regCheckbox } ) );
	};

	const submitForm = ( e ) => {
		e.preventDefault();
		dispatch( postAuthRegisterAsync( {
			confirmPassword: regConfirm.value,
			email: regEmail.value,
			password: regPassword.value,
			username: regNick.value
		} ) );
	};

	return (
		<form onSubmit={ submitForm }>
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
				text={ isFetching ? <SpinnerForAuthBtn/> : getTranslatedText( 'auth.signUp', lang ) }
				disabling={ disableReg }
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
