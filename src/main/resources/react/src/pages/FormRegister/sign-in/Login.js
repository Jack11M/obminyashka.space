import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';


import { route } from '../../../routes/routeConstants';
import { getTranslatedText } from '../../../components/local/localisation';
import Button from '../../../components/common/button/Button';
import CheckBox from '../../../components/common/checkbox';
import InputForAuth from '../../../components/common/input';

import SpinnerForAuthBtn from '../../../components/common/spinner/spinnerForAuthBtn';
import { startFetching, stopFetching } from '../../../store/ui/slice';
import { putToken } from '../../../store/auth/slice';

import { Extra, ExtraLink } from './loginStyle';
import { changeInputAuth, toggleButtonLog, translateErrorsAuth } from '../../../Utils';
import { postAuthLogin } from '../../../REST/Resources';

const Login = () => {
	const dispatch = useDispatch();
	const { isFetching } = useSelector( state => state.ui );
	const { lang } = useSelector( state => state.auth );
	const [ state, setState ] = useState( {
		logEmail: '',
		logPassword: '',
		logCheckbox: false,
		errors: []
	} );
	const [ disable, setDisable ] = useState( false );
	const { logEmail, logPassword, logCheckbox, errors } = state;

	useEffect(() => {
		const error = translateErrorsAuth(errors)
		setState({...state, errors: error})
		//eslint-disable-next-line react-hooks/exhaustive-deps
	}, [lang])

	useEffect( () => {
		const getPost = async () => {
			try {
				const { data } = await postAuthLogin( {
					usernameOrEmail: logEmail,
					password: logPassword
				} );
				dispatch(putToken({data, isCheck: logCheckbox}))
			} catch (error) {
				if (error.response.status === 400) {
					const { error: message } = error.response.data;
					setState( { ...state, errors: [ ...errors, { logEmail: message } ] } );
				}
			} finally {
				dispatch( stopFetching() );
			}
		};
		if (isFetching) {
			getPost().finally();
		}
		//eslint-disable-next-line react-hooks/exhaustive-deps
	}, [ dispatch, isFetching ] );

	useEffect( () => {
		if (toggleButtonLog( state )) {
			setDisable( false );
		} else {
			setDisable( true );
		}
	}, [ state ] );

	const changeInput = ( event ) => {
		const { name: key, value } = event.target;
		const newError = changeInputAuth( event, state );
		setState( {
			...state,
			errors: newError,
			[key]: value,
		} );
	};
	const changeCheckBox = () => {
		setState( prev => ({
			...prev,
			logCheckbox: !prev.logCheckbox
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
					text={ getTranslatedText( 'auth.logEmail', lang ) }
					name={ 'logEmail' }
					type={ 'text' }
					value={ logEmail }
					errors={ errors }
					click={ changeInput }
				/>
				<InputForAuth
					text={ getTranslatedText( 'auth.logPassword', lang ) }
					name={ 'logPassword' }
					type={ 'password' }
					value={ logPassword }
					errors={ errors }
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
				<ExtraLink to={ `${ route.login }${ route.signUp }` }>
					{ getTranslatedText( 'auth.noLogin', lang ) }
				</ExtraLink>
			</Extra>
			<Button
				text={ isFetching ? <SpinnerForAuthBtn/> : getTranslatedText( 'button.enter', lang ) }
				disabling={ disable }
				mb={ '64px' }
				bold
				lHeight={ '24px' }
				width={ '222px' }
				height={ '48px' }/>
		</form>
	);
};

export default Login;
