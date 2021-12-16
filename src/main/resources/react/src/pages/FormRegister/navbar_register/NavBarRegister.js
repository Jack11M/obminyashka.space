import React from 'react';
import { useSelector } from 'react-redux';
import { NavLink } from 'react-router-dom';

import { route } from 'routes/routeConstants';
import { getTranslatedText } from 'components/local/localisation';

import cls from './registerTabs.module.scss';

const NavBarRegister = () => {
  const { lang } = useSelector((state) => state.auth);
  return (
    <div className={cls.tabs}>
      <NavLink to={route.login} exact activeClassName={cls.active}>
        {getTranslatedText('auth.login', lang)}
      </NavLink>
      <NavLink
        to={`${route.login}${route.signUp}`}
        activeClassName={cls.active}
      >
        {getTranslatedText('auth.signUp', lang)}
      </NavLink>
    </div>
  );
};
export default NavBarRegister;
