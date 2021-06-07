import React, { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';
import { Link } from 'react-router-dom';

import TitleBigBlue from '../../../../components/common/title_Big_Blue/title_Big_Blue';
import Button from '../../../../components/common/button/Button';
import InputProfile from '../../components/inputProfile/inputProfile';
import { route } from '../../../../routes/routeConstants';
import { getTranslatedText } from '../../../../components/local/localisation';
import { changeInputAuth } from '../../../../Utils';

import './mySettings.scss';

const MySettings = () => {
	const { lang } = useSelector( ( state ) => state.auth );
	const [ password, setPassword ] = useState( {
		currentPassword: '',
		regPassword: '',
		regConfirm: '',
		errors: [],
		disable: false
	} );

	useEffect( () => {
		if (password.errors.length) {
			setPassword( p => ({ ...p, disable: true }) );
		} else {
			setPassword( p => ({ ...p, disable: false }) );
		}
	}, [ password.currentPassword, password.regPassword, password.regConfirm ] );

	const changePassword = ( event ) => {
		const { name: key, value } = event.target;
		const newError = changeInputAuth( event, password );
		setPassword( {
			...password,
			errors: newError,
			[key]: value
		} );
	};
	return (
		<>
			<form>
				<TitleBigBlue
					whatClass={ 'myProfile-title' }
					text={ getTranslatedText( `settings.changePassword`, lang ) }
				/>
				<div className="info-one">
					<InputProfile
						name={ 'currentPassword' }
						label={ getTranslatedText( `settings.currentPassword`, lang ) }
						type={ 'password' }
						value={ password.currentPassword }
						change={ changePassword }
						errors={ password.errors }
					/>
					<InputProfile
						name={ 'regPassword' }
						label={ getTranslatedText( `settings.newPassword`, lang ) }
						type={ 'password' }
						value={ password.regPassword }
						change={ changePassword }
						errors={ password.errors }
					/>
					<InputProfile
						name={ 'regConfirm' }
						label={ getTranslatedText( `settings.confirmPassword`, lang ) }
						type={ 'password' }
						value={ password.regConfirm }
						change={ changePassword }
						errors={ password.errors }
					/>
				</div>
				<Button
					text={ getTranslatedText( `button.save`, lang ) }
					whatClass={ 'btn-profile' }
					width={ '248px' }
					height={ '49px' }
					disabling={ password.disable }
				/>
			</form>
			<form>
				<TitleBigBlue
					whatClass={ 'myProfile-title' }
					text={ getTranslatedText( `settings.changeEmail`, lang ) }
				/>
				<div className="info-one">
					<InputProfile
						name={ 'mail' }
						label={ getTranslatedText( `settings.oldEmail`, lang ) }
						type={ 'email' }

					/>
					<InputProfile
						name={ 'newMail' }
						label={ getTranslatedText( `settings.newEmail`, lang ) }
						type={ 'email' }

					/>
				</div>
				<Button
					text={ getTranslatedText( `button.codeConfirm`, lang ) }
					whatClass={ 'btn-profile e-mail-button' }
					width={ '363px' } height={ '49px' }
				/>
			</form>
			<TitleBigBlue
				whatClass={ 'myProfile-title' }
				text={ getTranslatedText( `settings.remove`, lang ) }
			/>
			<p className="delete-text">
				{ getTranslatedText( `settings.describe`, lang ) } <Link
				to={ `${ route.userInfo }${ route.myProfile }` }>&nbsp;{ getTranslatedText( `settings.profile`, lang ) }</Link>
			</p>
			<div className={ 'btn-wrapper' }>
				<Button
					text={ getTranslatedText( `button.remove`, lang ) }
					whatClass={ 'btn-profile' }
					width={ '248px' }
				/>
			</div>
		</>
	);
};

export default React.memo( MySettings );
