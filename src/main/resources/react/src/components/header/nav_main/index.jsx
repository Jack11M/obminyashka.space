import { Link } from 'react-router-dom';

import { ButtonAdv } from 'components/common/buttons';
import { getTranslatedText } from 'components/local/localization';
import { ReactComponent as SearchSvg } from 'assets/icons/search.svg';

import NavCategory from '../nav_category';

import './navMain.scss';

const NavMain = () => (
  <div className="navbar-main-inner">
    <div className="wrapper">
      <div className="navbar-main">
        <Link to="/" className="logo" />

        <div className="navbar-main__select">
          <div className="navbar-main__select-top">
            {getTranslatedText('header.categories')}
          </div>
          <div className="navbar-main__select-bottom">
            {getTranslatedText('header.categories')}
          </div>
          <NavCategory />
        </div>

        <div className="navbar-main-search">
          <input
            id="search"
            type="text"
            placeholder={`${getTranslatedText('header.iSearch')} ...`}
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

export default NavMain;
