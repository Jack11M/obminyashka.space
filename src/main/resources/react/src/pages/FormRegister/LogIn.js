import React from 'react';
import { useDispatch, useSelector } from 'react-redux';

import { getTranslatedText } from '../../components/local/localisation';
import CheckBox from '../../components/checkbox';
import Button from '../../components/button/Button';
import InputForAuth from './InputForAuth';
import { toggleCheckBox } from '../../redux/auth/action';

import { Extra, ExtraLink } from './loginStyle';

const LogIn = () => {
	const dispatch = useDispatch();
	const {logCheckbox} = useSelector(state => state.auth)

	const changeCheckBox = () => {
		dispatch( toggleCheckBox( { logCheckbox: !logCheckbox }) );
	};

	return (
		<form>
			<div>
				<InputForAuth
					text={ getTranslatedText( 'auth.logEmail' ) }
					name={ 'logName' }
					type={ 'text' }
					error={ '' }
				/>
				<InputForAuth
					text={ getTranslatedText( 'auth.logPassword' ) }
					name={ 'logPassword' }
					type={ 'password' }
					error={ '' }
				/>
			</div>
			<Extra>
				<CheckBox
					text={ getTranslatedText( 'auth.remember' ) }
					margin={ '0 0 44px 0' }
					fs={ '14px' }
					checked={ logCheckbox }
					click={ changeCheckBox }
				/>
				<ExtraLink to={ '/logIn/signUp' }>
					{ getTranslatedText( 'auth.noLogin' ) }
				</ExtraLink>
			</Extra>
			<Button
				text={ getTranslatedText( 'button.enter' ) }
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