import React from "react";

import TitleBigBlue from "../../../../components/title_Big_Blue/title_Big_Blue";
import Button from '../../../../components/button/Button';
import "./mySettings.scss";

const MySettings = () => {
    return (
        <form>
            <TitleBigBlue whatClass={"myProfile-title"} text={"Сменить пароль"}/>
            <div className="info-one">
                <div className="row">
                    <label htmlFor="name">Текущий пароль:</label>
                    <input type="password" name="name" id="name"/>
                </div>
                <div className="row">
                    <label htmlFor="lastName">Новый пароль:</label>
                    <input type="password" name="lastName" id="lastName"/>
                </div>
                <div className="row">
                    <label htmlFor="city">Повторите пароль:</label>
                    <input type="password" name="city" id="city"/>
                </div>
            </div>
            <Button text={"Сохранить"} whatClass={"btn-profile"}/>
            <TitleBigBlue whatClass={"myProfile-title"} text={"Сменить e-mail"}/>
            <div className="info-one">
                <div className="row">
                    <label htmlFor="name">Старый E-Mail:</label>
                    <input type="email" name="name" id="name"/>
                </div>
                <div className="row">
                    <label htmlFor="lastName">Новый E-Mail:</label>
                    <input type="email" name="lastName" id="lastName"/>
                </div>
            </div>
            <Button text={"Отправить код подтверждения"} whatClass={"btn-profile e-mail-button"}/>
            <TitleBigBlue whatClass={"myProfile-title"} text={"Удалить аккаунт"}/>
            <p className="delete-text">
                Внимание! После удаления учетной записи, ваш профиль будет полностью удален из каталога сайта
                и многие функции перестанут быть доступными. Вы можете поменять контактные данные и номера телефонов
                в разделе меню <a href="#">Профиль</a>
            </p>
            <Button text={"Удалить аккаунт"} whatClass={"btn-profile"}/>
        </form>
    )
};

export default MySettings;
