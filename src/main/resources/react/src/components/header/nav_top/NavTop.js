import React from 'react';
import { useSelector } from 'react-redux';
import { Link } from 'react-router-dom';

import Avatar from 'components/common/avatar/avatar.js';
import SelectLang from 'components/selectLang';
import { getTranslatedText } from 'components/local/localisation';
import { route } from 'routes/routeConstants';
import { ReactComponent as HeartSvg } from 'assets/icons/heart.svg';

import './navtop.scss';

const NavTop = () => {
  const { lang } = useSelector((state) => state.auth);
  const { isAuthed, profile } = useSelector((state) => state.auth);

  return (
    <div className="navbar-top-inner">
      <div className="wrapper">
        <div className="navbar-top">
          <div className="navbar-top-links">
            <Link to="/" className="navbar-top-link">
              {getTranslatedText('header.about', lang)}
            </Link>
            <Link to={route.home} className="navbar-top-link">
              <HeartSvg className="navbar-top-link-svg"/>
              {getTranslatedText('header.goodness', lang)}
            </Link>
          </div>
          <div id="personalArea">
            <Link to={isAuthed ? route.userInfo : route.login}>
              <Avatar whatIsClass={'user-photo'} width={30} height={28} />
              <span>
                {profile?.username || getTranslatedText('header.myOffice', lang)}
              </span>
            </Link>
            <SelectLang />
          </div>
        </div>
      </div>
    </div>
  );
};

export default NavTop;
