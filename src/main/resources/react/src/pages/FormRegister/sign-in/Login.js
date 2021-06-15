import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Formik } from 'formik';
import * as yup from 'yup';

import { route } from '../../../routes/routeConstants';
import { getTranslatedText } from '../../../components/local/localisation';
import Button from '../../../components/common/button/Button';
import CheckBox from '../../../components/common/checkbox';
import InputForAuth from '../../../components/common/input';
import { postAuthLogin } from '../../../REST/Resources';
import { putToken, unauthorized } from '../../../store/auth/slice';

import SpinnerForAuthBtn from '../../../components/common/spinner/spinnerForAuthBtn';
import { Extra, ExtraLink } from './loginStyle';

const Login = () => {
	const dispatch = useDispatch();
	const { lang } = useSelector( state => state.auth );
	const [ checkbox, setCheckbox ] = useState( false );
	const [ loading, setLoading ] = useState( false );

	const changeCheckBox = () => {
		setCheckbox( prev => !prev );
	};

	const validationLoginSchema = yup.object().shape( {
		usernameOrEmail:
			yup.string()
			.required( 'Поле обязательно' )
			.default( () => '' ),
		password:
			yup.string()
			.required( 'Поле обязательно' )
			.default( () => '' )
	} );
	const initialLoginValues = validationLoginSchema.cast( {} );


	return (
		<form>
			<Formik
				initialValues={ initialLoginValues }
				validationSchema={ validationLoginSchema }
				validateOnBlur
				enableReinitialize
				onSubmit={ (async ( dataFormik, onSubmitProps ) => {
					if(loading) return;
					setLoading( true );
					try {
						const { data } = await postAuthLogin( dataFormik );
						dispatch( putToken( { data, checkbox } ) );
					} catch (err) {
						if (err.response.status === 401) {
							dispatch( unauthorized() );
						}
						if (err.response.status === 400) {
							onSubmitProps.setErrors( { usernameOrEmail: err.response.data.error } );
						}
					}finally {
						setLoading( false );
					}
				}) }
			>
				{ ( { errors, handleSubmit, isValid, dirty } ) => {
					return (
						<>
							<div>
								<InputForAuth
									text={ getTranslatedText( 'auth.logEmail', lang ) }
									name={ 'usernameOrEmail' }
									type={ 'text' }
								/>
								<InputForAuth
									text={ getTranslatedText( 'auth.logPassword', lang ) }
									name={ 'password' }
									type={ 'password' }
								/>
							</div>
							<Extra>
								<CheckBox
									text={ getTranslatedText( 'auth.remember', lang ) }
									margin={ '0 0 44px 0' }
									fs={ '14px' }
									checked={ checkbox }
									click={ changeCheckBox }
								/>
								<ExtraLink to={ `${ route.login }${ route.signUp }` }>
									{ getTranslatedText( 'auth.noLogin', lang ) }
								</ExtraLink>
							</Extra>
							<Button
								text={ loading ? <SpinnerForAuthBtn/> : getTranslatedText( 'button.enter', lang ) }
								mb={ '64px' }
								type={ 'submit' }
								bold
								lHeight={ '24px' }
								width={ '222px' }
								height={ '48px' }
								disabling={  !isValid && !dirty }
								click={ !errors.usernameOrEmail ? handleSubmit : null }
							/>
						</>);
				} }
			</Formik>
		</form>
	);
};

export default Login;
