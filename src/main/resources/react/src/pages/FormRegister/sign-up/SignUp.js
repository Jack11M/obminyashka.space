import React, { useState } from 'react';
import { useSelector } from 'react-redux';
import { useHistory } from 'react-router-dom';
import { Formik } from 'formik';
import * as yup from 'yup';

import InputForAuth from '../../../components/common/input';
import CheckBox from '../../../components/common/checkbox';
import Button from '../../../components/common/button/Button';
import { getTranslatedText } from '../../../components/local/localisation';
import { EMAIL_REG_EXP, PASSWORD_ALT_CODE_EXP, PASSWORD_REG_EXP, USERNAME_ALT_CODE_EXP } from '../../../config';
import { postAuthRegister } from '../../../REST/Resources';
import { route } from '../../../routes/routeConstants';

import SpinnerForAuthBtn from '../../../components/common/spinner/spinnerForAuthBtn';
import { Extra } from '../sign-in/loginStyle';

const SignUp = () => {
	const history = useHistory();
	const { lang } = useSelector( state => state.auth );
	const [ loading, setLoading ] = useState( false );
	const [ checkbox, setCheckbox ] = useState( false );


	// useEffect( () => {
	// 	const error = translateErrorsAuth( errors );
	// 	setState( { ...state, errors: error } );
	// 	//eslint-disable-next-line react-hooks/exhaustive-deps
	// }, [ lang ] );

	// useEffect( () => {
	// 	const get = async () => {
	// 		try {
	// 			await postAuthRegister( {
	// 				email: regEmail,
	// 				username: regNick,
	// 				password: regPassword,
	// 				confirmPassword: regConfirm
	// 			} );
	// 			history.push( route.login );
	// 		} catch (error) {
	// 			if (error.response.status === 400) {
	// 				const { error: message } = error.response.data;
	// 				message.includes( 'login' )
	// 				||
	// 				message.includes( 'логіном' )
	// 				||
	// 				message.includes( 'имя' )
	// 					?
	// 					setState( { ...state, errors: [ ...errors, { regNick: message } ] } )
	// 					:
	// 					setState( { ...state, errors: [ ...errors, { regEmail: message } ] } );
	// 			}
	// 		} finally {
	// 			setLoadingFetch( false );
	// 		}
	// 	};
	// 	if (loadingFetch) {
	// 		get().finally();
	// 	}
	// 	//eslint-disable-next-line react-hooks/exhaustive-deps
	// }, [ dispatch, isFetching ] );


	// useEffect( () => {
	// 	if (toggleButtonReg( state )) {
	// 		setDisable( false );
	// 	} else {
	// 		setDisable( true );
	// 	}
	// }, [ state ] );
	//
	// useEffect( () => {
	// 	changeInput( { target: { name: 'regConfirm', value: regConfirm } } );
	// 	//eslint-disable-next-line react-hooks/exhaustive-deps
	// }, [ regPassword, regConfirm ] );
	//
	// const changeInput = ( event ) => {
	// 	const { name: key, value } = event.target;
	// 	const newError = isErrorArray( event, state );
	// 	setState( {
	// 		...state,
	// 		[key]: value,
	// 		errors: newError
	// 	} );
	// };
	//
	const changeCheckBox = () => {
		setCheckbox( prev => !prev );
	};

	const validationRegisterSchema = yup.object().shape( {
		email:
			yup.string()
			.email( 'Не верный формат Email' )
			.required( 'Поле обязательно' )
			.matches( EMAIL_REG_EXP, 'must be at least 129 characters' )
			.default( () => '' ),
		username:
			yup.string()
			.required( 'Поле обязательно' )
			.min( 2, 'must be at least 2 characters' )
			.max( 50, 'must be no more than 50 characters' )
			.matches( USERNAME_ALT_CODE_EXP, 'Не должно быть Alt code' )
			.default( () => '' ),
		password:
			yup.string()
			.required( 'Поле обязательно' )
			.min( 8, 'must be at least 8 characters' )
			.max( 30, 'must be at least 30 characters' )
			.matches( PASSWORD_REG_EXP, 'Большие и малые латинские буквы и цифры.' )
			.matches( PASSWORD_ALT_CODE_EXP, 'Не должно быть Alt code' )
			.default( () => '' ),
		confirmPassword:
			yup.string()
			.oneOf( [ yup.ref( 'password' ) ], 'Пароли не совпадают' )
			.required( 'Поле обязательно' )
			.default( () => '' )
	} );
	const initialRegisterValues = validationRegisterSchema.cast( {} );

	return (
		<form>
			<Formik
				initialValues={ initialRegisterValues }
				validationSchema={ validationRegisterSchema }
				validateOnBlur
				onSubmit={ (async ( dataFormik, onSubmitProps ) => {
					setLoading( true );
					try {
						await postAuthRegister( dataFormik );
						setLoading( false );

					} catch (err) {
						if (err.response.status === 400) {
							const { error: message } = err.response.data;
							const field = message.includes( 'login' ) || message.includes( 'логіном' ) || message.includes( 'имя' )
								? 'username' : 'email';
							onSubmitProps.setErrors( { [field]: message } );
						}
					} finally {
						setLoading( false );
						history.push( route.login );
					}
				}) }
			>

				{ ( { errors, isSubmitting, handleSubmit, isValid, dirty } ) => {
					return (
						<>
							<div>
								<InputForAuth
									text={ getTranslatedText( 'auth.regEmail', lang ) }
									name={ 'email' }
									type={ 'email' }
								/>
								<InputForAuth
									text={ getTranslatedText( 'auth.regLogin', lang ) }
									name={ 'username' }
									type={ 'text' }

								/>
								<InputForAuth
									text={ getTranslatedText( 'auth.regPassword', lang ) }
									name={ 'password' }
									type={ 'password' }

								/>
								<InputForAuth
									text={ getTranslatedText( 'auth.regConfirm', lang ) }
									name={ 'confirmPassword' }
									type={ 'password' }
								/>
							</div>
							<Extra>
								<CheckBox
									text={ getTranslatedText( 'auth.agreement', lang ) }
									margin={ '0 0 44px 0' }
									fs={ '14px' }
									checked={ checkbox }
									click={ changeCheckBox }
								/>
							</Extra>
							<Button
								text={ loading ? <SpinnerForAuthBtn/> : getTranslatedText( 'auth.signUp', lang ) }
								mb={ '44px' }
								bold
								type={ 'submit' }
								lHeight={ '24px' }
								width={ '222px' }
								height={ '48px' }
								disabling={ !checkbox || isSubmitting || !dirty && !isValid  }
								click={ !errors.email || !errors.username ? handleSubmit : null }
							/>
						</>
					);
				} }

			</Formik>
		</form>
	);
};

export default SignUp;
