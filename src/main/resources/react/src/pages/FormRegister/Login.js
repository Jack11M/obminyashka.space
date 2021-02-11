import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useLocation } from 'react-router-dom';

import { getTranslatedText } from '../../components/local/localisation';
import CheckBox from '../../components/checkbox';
import Button from '../../components/button/Button';
import InputForAuth from './InputForAuth';
import {
	clearValueLogin,
	clearValueSignUp,
	inputChangeAuth,
	postAuthLoginAsync,
	toggleCheckBox,
	toggleDisableButtonLog
} from '../../redux/auth/action';

import { Extra, ExtraLink } from './loginStyle';
import SpinnerForAuthBtn from '../../components/spinner/spinnerForAuthBtn';
import { route } from '../../routes/routeConstants';

const Login = () => {
	const location = useLocation();
	const dispatch = useDispatch();
	const { lang, logEmail, logPassword, logCheckbox, disableLog } = useSelector( state => state.auth );
	const { isFetching } = useSelector( state => state.ui );

	useEffect( () => {
		dispatch( clearValueLogin() );
		dispatch( clearValueSignUp() );
	}, [ dispatch, location.pathname ] );

	useEffect( () => {
		dispatch( toggleDisableButtonLog() );
	}, [ dispatch, logEmail, logPassword ] );

	const changeInput = ( e ) => {
		if (e.target.name === 'logPassword') dispatch( inputChangeAuth( [ 'logEmail', logEmail.value ] ) );
		dispatch( inputChangeAuth( [ e.target.name, e.target.value ] ) );
	};

	const changeCheckBox = () => {
		dispatch( toggleCheckBox( { logCheckbox: !logCheckbox } ) );
	};

	const submitForm = ( e ) => {
		e.preventDefault();
		dispatch( postAuthLoginAsync( {
			usernameOrEmail: logEmail.value,
			password: logPassword.value
		} ) );
	};

	return (
		<form onSubmit={ submitForm }>
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
				<ExtraLink to={ `${route.login}${route.signUp}` }>
					{ getTranslatedText( 'auth.noLogin', lang ) }
				</ExtraLink>
			</Extra>
			<Button
				text={ isFetching ? <SpinnerForAuthBtn/> : getTranslatedText( 'button.enter', lang ) }
				disabling={ disableLog }
				mb={ '64px' }
				bold
				lHeight={ '24px' }
				width={ '222px' }
				height={ '48px' }/>
		</form>
	);
};

export default Login;
