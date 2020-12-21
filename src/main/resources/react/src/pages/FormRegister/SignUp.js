import React from 'react';
import { useDispatch, useSelector } from 'react-redux';

import CheckBox from '../../components/checkbox';
import Button from '../../components/button/Button';
import InputForAuth from './InputForAuth';
import { getTranslatedText } from '../../components/local/localisation';
import { toggleCheckBox } from '../../redux/auth/action';

import { Extra } from './loginStyle';


const SignUp = () => {
	const dispatch = useDispatch();
	const {regCheckbox} = useSelector(state => state.auth)

	const changeCheckBox = () => {
		dispatch( toggleCheckBox( { regCheckbox: !regCheckbox }) );
	};

	return (
		<form>
			<div>
				<InputForAuth
					text={ getTranslatedText( 'auth.regEmail' ) }
					name={ 'regEmail' }
					type={ 'text' }
					error={ '' }
				/>
				<InputForAuth
					text={ getTranslatedText( 'auth.regLogin' ) }
					name={ 'regLogin' }
					type={ 'text' }
					error={ '' }
				/>
				<InputForAuth
					text={ getTranslatedText( 'auth.regPassword' ) }
					name={ 'regPassword' }
					type={ 'password' }
					error={ '' }
				/>
				<InputForAuth
					text={ getTranslatedText( 'auth.regConfirm' ) }
					name={ 'regConfirm' }
					type={ 'password' }
					error={ '' }
				/>
			</div>
			<Extra>
				<CheckBox
					text={ getTranslatedText( 'auth.agreement' ) }
					margin={ '0 0 44px 0' }
					fs={ '14px' }
					checked={ regCheckbox }
					click={changeCheckBox}
				/>
			</Extra>
			<Button
				text={ getTranslatedText( 'auth.signUp' ) }
				disabling={ false }
				mb={ '44px' }
				bold
				lHeight={ '24px' }
				width={ '222px' }
				height={ '48px' }/>
		</form>
	);
};

export default SignUp;