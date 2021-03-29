import React from "react";
import { NavLink } from "react-router-dom";
import { useSelector } from 'react-redux';

import { route } from '../../../routes/routeConstants';
import { getTranslatedText } from '../../../components/local/localisation';

import "./tabs.scss";


const Tabs = props => {
  const { url } = props;
  const { lang } = useSelector( state => state.auth );
  return (
    <div className="tabs">
      <NavLink to={`${url}`} exact >
        <i className={"icon-activity"} />
        {getTranslatedText('ownInfo.panelMyActivity',lang)}
        <i className="active__cycle" />
      </NavLink>

      <NavLink to={`${url}${route.myProfile}`}>
        <i className={"icon-profile"} />
        {getTranslatedText('ownInfo.panelMyProfile',lang)}
        <i className="active__cycle" />
      </NavLink>

      <NavLink to={`${url}${route.myFavorite}`}>
        <i className={"icon-star"} />
        {getTranslatedText('ownInfo.panelMyFavorite',lang)}
        <i className="active__cycle" />
      </NavLink>

      <NavLink to={`${url}${route.mySettings}`}>
        <i className={"icon-settings"} />
        {getTranslatedText('ownInfo.panelMySettings',lang)}
        <i className="active__cycle" />
      </NavLink>

      <NavLink to={`${url}${route.exit}`}>
        <i className={"icon-logout"} />
        {getTranslatedText('ownInfo.panelExit',lang)}
        <i className="active__cycle" />
      </NavLink>
    </div>
  );
};

export default Tabs;
