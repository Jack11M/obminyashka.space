import React from 'react';
import { Link } from 'react-router-dom';

import TitleBigBlue from '../../../../components/title_Big_Blue/title_Big_Blue';
import Button from '../../../../components/button/Button';
import InputProfile from '../../components/inputProfile/inputProfile';
import { route } from '../../../../routes/routeConstants';

import './mySettings.scss';

const MySettings = () => {
	return (
		<>
			<form>
				<TitleBigBlue whatClass={ 'myProfile-title' } text={ 'Сменить пароль' }/>
				<div className="info-one">
					<InputProfile name={ 'currentPassword' } label={ 'Текущий пароль:' } type={ 'password' }/>
					<InputProfile name={ 'newPassword' } label={ 'Новый пароль:' } type={ 'password' }/>
					<InputProfile name={ 'newPasswordRepeat' } label={ 'Повторите пароль:' } type={ 'password' }/>
				</div>
				<Button text={ 'Сохранить' } whatClass={ 'btn-profile' } width={ '248px' } height={ '49px' }/>
			</form>
			<form>
				<TitleBigBlue whatClass={ 'myProfile-title' } text={ 'Сменить e-mail' }/>
				<div className="info-one">
					<InputProfile name={ 'mail' } label={ 'Старый E-Mail:' } type={ 'email' }/>
					<InputProfile name={ 'newMail' } label={ 'Новый E-Mail:' } type={ 'email' }/>
				</div>
				<Button text={ 'Отправить код подтверждения' } whatClass={ 'btn-profile e-mail-button' } width={ '363px' } height={ '49px' }/>
			</form>
			<TitleBigBlue whatClass={ 'myProfile-title' } text={ 'Удалить аккаунт' }/>
			<p className="delete-text">
				Внимание! После удаления учетной записи, ваш профиль будет полностью удален из каталога сайта
				и многие функции перестанут быть доступными. Вы можете поменять контактные данные и номера телефонов
				в разделе меню <Link to={ `${route.userInfo}${route.myProfile}` }>Профиль</Link>
			</p>
			<div className={ 'btn-wrapper' }>
				<Button text={ 'Удалить аккаунт' } whatClass={ 'btn-profile' } width={ '248px' }/>
			</div>
		</>
	);
};

export default MySettings;
