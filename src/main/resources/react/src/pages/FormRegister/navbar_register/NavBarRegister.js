import React from 'react';
import { useSelector } from 'react-redux';
import { Outlet } from 'react-router-dom';

import { route } from 'routes/routeConstants';
import { getTranslatedText } from 'components/local/localisation';

import { CustomLink } from './custom-link';
import cls from './registerTabs.module.scss';

const NavBarRegister = () => {
  const { lang } = useSelector((state) => state.auth);

  return (
    <>
      <div className={cls.tabs}>
        <CustomLink to="">{getTranslatedText('auth.login', lang)}</CustomLink>

        <CustomLink to={route.signUp}>
          {getTranslatedText('auth.signUp', lang)}
        </CustomLink>
      </div>

      <Outlet />
    </>
  );
};
export default NavBarRegister;
