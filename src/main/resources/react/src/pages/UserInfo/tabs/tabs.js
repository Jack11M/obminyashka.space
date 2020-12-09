import React from "react";
import { NavLink } from "react-router-dom";

import "./tabs.scss";

const Tabs = props => {
  const { url } = props;

  return (
    <div className="tabs">
      <NavLink to={`${url}`} exact>
        <i className={"icon-activity"} />
        Моя активность
        <i className="active__cycle" />
      </NavLink>

      <NavLink to={`${url}/my_profile`}>
        <i className={"icon-profile"} />
        Мой профиль
        <i className="active__cycle" />
      </NavLink>

      <NavLink to={`${url}/my_favorites`}>
        <i className={"icon-star"} />
        Избранное
        <i className="active__cycle" />
      </NavLink>

      <NavLink to={`${url}/my_settings`}>
        <i className={"icon-settings"} />
        Настройки
        <i className="active__cycle" />
      </NavLink>

      <NavLink to={`${url}/exit`}>
        <i className={"icon-logout"} />
        Выйти
        <i className="active__cycle" />
      </NavLink>
    </div>
  );
};

export default Tabs;
