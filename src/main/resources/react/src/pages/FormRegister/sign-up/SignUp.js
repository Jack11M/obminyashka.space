import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useHistory } from 'react-router-dom';

import InputForAuth from '../../../components/common/input';
import CheckBox from '../../../components/common/checkbox';
import Button from '../../../components/common/button/Button';
import { getTranslatedText } from '../../../components/local/localisation';
import SpinnerForAuthBtn from '../../../components/common/spinner/spinnerForAuthBtn';
import { postAuthRegister } from '../../../REST/Resources';
import { changeInputAuth, toggleButtonReg, translateErrorsAuth } from '../../../Utils';
import { startFetching, stopFetching } from '../../../store/ui/slice';
import { route } from '../../../routes/routeConstants';

import { Extra } from '../sign-in/loginStyle';


const SignUp = () => {
	const history = useHistory();
	const dispatch = useDispatch();
	const { isFetching } = useSelector( state => state.ui );
	const { lang } = useSelector( state => state.auth );
	const [ disable, setDisable ] = useState( false );
	const [ state, setState ] = useState( {
		regEmail: '',
		regNick: '',
		regPassword: '',
		regConfirm: '',
		errors: [],
		regCheckbox: false
	} );
	const { regEmail, regNick, regPassword, regConfirm, errors, regCheckbox } = state;


	useEffect(() => {
		const error = translateErrorsAuth(errors)
		setState({...state, errors: error})
		//eslint-disable-next-line react-hooks/exhaustive-deps
	}, [lang])

	useEffect( () => {
		const get = async () => {
			try {
				await postAuthRegister( {
					email: regEmail,
					username: regNick,
					password: regPassword,
					confirmPassword: regConfirm
				} );
				history.push( route.login );
			} catch (error) {
				if (error.response.status === 400) {
					const { error: message } = error.response.data;
					message.includes( 'login' )
					||
					message.includes( 'логіном' )
					||
					message.includes( 'имя' )
						?
						setState( { ...state, errors: [ ...errors, { regNick: message } ] } )
						:
						setState( { ...state, errors: [ ...errors, { regEmail: message } ] } );
				}
			} finally {
				dispatch( stopFetching() );
			}
		};
		if (isFetching) {
			get().finally();
		}
		//eslint-disable-next-line react-hooks/exhaustive-deps
	}, [ dispatch, isFetching ] );


	useEffect( () => {
		if (toggleButtonReg( state )) {
			setDisable( false );
		} else {
			setDisable( true );
		}
	}, [ state ] );

	useEffect( () => {
		changeInput( { target: { name: 'regConfirm', value: regConfirm } } );
		//eslint-disable-next-line react-hooks/exhaustive-deps
	}, [ regPassword, regConfirm ] );

	const changeInput = ( event ) => {
		const { name: key, value } = event.target;
		const newError = changeInputAuth( event, state );
		setState( {
			...state,
			[key]: value,
			errors: newError
		} );
	};

	const changeCheckBox = () => {
		setState( prev => ({
			...prev,
			regCheckbox: !prev.regCheckbox
		}) );
	};

	const submitForm = ( e ) => {
		e.preventDefault();
		if(!isFetching){
			dispatch( startFetching() );
		}
	};

	return (
		<form onSubmit={ submitForm }>
			<div>
				<InputForAuth
					text={ getTranslatedText( 'auth.regEmail', lang ) }
					name={ 'regEmail' }
					type={ 'text' }
					value={ regEmail }
					errors={ errors }
					click={ changeInput }
				/>
				<InputForAuth
					text={ getTranslatedText( 'auth.regLogin', lang ) }
					name={ 'regNick' }
					type={ 'text' }
					value={ regNick }
					errors={ errors }
					click={ changeInput }
				/>
				<InputForAuth
					text={ getTranslatedText( 'auth.regPassword', lang ) }
					name={ 'regPassword' }
					type={ 'password' }
					value={ regPassword }
					errors={ errors }
					click={ changeInput }
				/>
				<InputForAuth
					text={ getTranslatedText( 'auth.regConfirm', lang ) }
					name={ 'regConfirm' }
					type={ 'password' }
					value={ regConfirm }
					errors={ errors }
					click={ changeInput }
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
				disabling={ disable }
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
