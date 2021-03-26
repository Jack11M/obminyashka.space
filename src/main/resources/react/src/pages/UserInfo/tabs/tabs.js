import React from "react";
import { NavLink } from "react-router-dom";
import {useDispatch, useSelector} from "react-redux";
import { route } from '../../../routes/routeConstants';

import "./tabs.scss";
import {toggleModal, checkModal} from "../../../redux/profile/profileAction";

const Tabs = props => {
  const { url } = props;
  const {modalIsOpening} = useSelector((state) => state.profileMe);
  const dispatch = useDispatch();

  const toggle = () => {
    dispatch(toggleModal());
    dispatch(checkModal(2));
  }

  const changeClass = () => {
    dispatch(checkModal(0));
  }

  return (
    <div className="tabs">
      <NavLink to={`${url}`} exact >
        <i className={"icon-activity"} />
        Моя активность
        <i className="active__cycle" />
      </NavLink>

      <NavLink to={`${url}${route.myProfile}`} onClick={() => changeClass()}>
        <i className={"icon-profile"}/>
        Мой профиль
        <i className="active__cycle"/>
      </NavLink>

      <NavLink to={`${url}${route.myFavorite}`} onClick={() => changeClass()}>
        <i className={"icon-star"}/>
        Избранное
        <i className="active__cycle"/>
      </NavLink>

      <NavLink to={`${url}${route.mySettings}`} className={modalIsOpening === 1 ? "active" : ""}>
        <i className={"icon-settings"}/>
        Настройки
        <i className="active__cycle"/>
      </NavLink>

      <NavLink to={`${url}${route.exit}`} onClick={() => toggle()}>
        <i className={"icon-logout"}/>
        Выйти
        <i className="active__cycle"/>
      </NavLink>
    </div>
  );
};

export default Tabs;
