import { useSelector } from 'react-redux';

import { route } from 'routes/routeConstants';
import { Avatar } from 'components/common/avatar';
import { CustomSelect } from 'components/selectLang';
import { getAuth, getProfile, getLang } from 'store/auth/slice';
import { getTranslatedText } from 'components/local/localization';
import { ReactComponent as HeartSvg } from 'assets/icons/heart.svg';

import './navtop.scss';

import * as Styles from './styles';

const NavTop = () => {
  const lang = useSelector(getLang);
  const isAuthed = useSelector(getAuth);
  const profile = useSelector(getProfile);

  return (
    <Styles.Div>
      <Styles.Wrapper>
        <Styles.DivTop>
          <Styles.WrapLinks>
            <Styles.NavTopLink to="/">
              {getTranslatedText('header.about', lang)}
            </Styles.NavTopLink>

            <Styles.NavTopLink to={route.home}>
              <HeartSvg />
              {getTranslatedText('header.goodness', lang)}
            </Styles.NavTopLink>
          </Styles.WrapLinks>

          <Styles.WrapPersonal>
            <Styles.LoginLink to={isAuthed ? route.userInfo : route.login}>
              <Avatar whatIsClass="user-photo" width={30} height={28} />

              <Styles.ProfileSpan>
                {profile?.username ||
                  getTranslatedText('header.myOffice', lang)}
              </Styles.ProfileSpan>
            </Styles.LoginLink>
            <CustomSelect />
          </Styles.WrapPersonal>
        </Styles.DivTop>
      </Styles.Wrapper>
    </Styles.Div>
  );
};

export default NavTop;
