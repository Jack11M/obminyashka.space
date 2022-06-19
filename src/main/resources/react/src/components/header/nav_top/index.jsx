import { Link } from 'react-router-dom';
import { useSelector } from 'react-redux';

import { route } from 'routes/routeConstants';
import { Avatar } from 'components/common/avatar';
import { CustomSelect } from 'components/selectLang';
import { getAuth, getAuthProfile } from 'store/auth/slice';
import { getTranslatedText } from 'components/local/localization';
import { ReactComponent as HeartSvg } from 'assets/icons/heart.svg';

import './navtop.scss';

const NavTop = () => {
  const isAuthed = useSelector(getAuth);
  const profile = useSelector(getAuthProfile);

  return (
    <div className="navbar-top-inner">
      <div className="wrapper">
        <div className="navbar-top">
          <div className="navbar-top-links">
            <Link to="/" className="navbar-top-link">
              {getTranslatedText('header.about')}
            </Link>

            <Link to={route.home} className="navbar-top-link">
              <HeartSvg className="navbar-top-link-svg" />
              {getTranslatedText('header.goodness')}
            </Link>
          </div>

          <div id="personalArea">
            <Link to={isAuthed ? route.userInfo : route.login}>
              <Avatar whatIsClass="user-photo" width={30} height={28} />

              <span>
                {profile?.username || getTranslatedText('header.myOffice')}
              </span>
            </Link>
            <CustomSelect />
          </div>
        </div>
      </div>
    </div>
  );
};

export default NavTop;
