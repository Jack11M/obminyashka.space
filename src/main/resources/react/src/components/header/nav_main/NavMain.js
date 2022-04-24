import React from 'react';
import { useSelector } from 'react-redux';
import { Link } from 'react-router-dom';

import NavCategory from '../nav_category/NavCategory.js';
import { getTranslatedText } from 'components/local/localization';
import { ReactComponent as SearchSvg } from 'assets/icons/search.svg';
import ButtonAdv from 'components/common/buttons/buttonAdv/ButtonAdv.js';

import './navMain.scss';

const NavMain = () => {
  const { lang } = useSelector((state) => state.auth);
  return (
    <div className="navbar-main-inner">
      <div className="wrapper">
        <div className="navbar-main">
          <Link to="/" className="logo" />
          <div className="navbar-main__select">
            <div className={'navbar-main__select-top'}>
              {getTranslatedText('header.categories', lang)}
            </div>
            <div className={'navbar-main__select-bottom'}>
              {getTranslatedText('header.categories', lang)}
            </div>
            <NavCategory />
          </div>
          <div className="navbar-main-search">
            <input
              type="text"
              id="search"
              placeholder={`${getTranslatedText('header.iSearch', lang)} ...`}
            />
            <label htmlFor="search" className="circle">
              <SearchSvg />
            </label>
          </div>
          <ButtonAdv type="link" />
        </div>
      </div>
    </div>
  );
};

export default NavMain;
