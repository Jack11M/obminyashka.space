import React from "react";
import { NavLink } from "react-router-dom";

import { route } from '../../../routes/routeConstants';

import "./tabs.scss";


const Tabs = props => {
  const { url } = props;

  return (
    <div className="tabs">
      <NavLink to={`${url}${route.activity}`} exact >
        <i className={"icon-activity"} />
        Моя активность
        <i className="active__cycle" />
      </NavLink>

      <NavLink to={`${url}${route.myProfile}`}>
        <i className={"icon-profile"} />
        Мой профиль
        <i className="active__cycle" />
      </NavLink>

      <NavLink to={`${url}${route.myFavorite}`}>
        <i className={"icon-star"} />
        Избранное
        <i className="active__cycle" />
      </NavLink>

      <NavLink to={`${url}${route.mySettings}`}>
        <i className={"icon-settings"} />
        Настройки
        <i className="active__cycle" />
      </NavLink>

      <NavLink to={`${url}${route.exit}`}>
        <i className={"icon-logout"} />
        Выйти
        <i className="active__cycle" />
      </NavLink>
    </div>
  );
};

export default Tabs;
