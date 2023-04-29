import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Avatar, Icon, Responsive } from 'obminyashka-components';

import { getAuthed } from 'src/store/auth/slice';
import { route } from 'src/routes/routeConstants';
import { getProfile } from 'src/store/profile/slice';
import { getUserThunk } from 'src/store/profile/thunk';
import { SelectLanguage } from 'src/components/selectLang';
import { getTranslatedText } from 'src/components/local/localization';

import * as Styles from './styles';

const NavTop = () => {
  const dispatch = useDispatch();
  const isAuthed = useSelector(getAuthed);
  const profile = useSelector(getProfile);

  useEffect(() => {
    if (!profile && isAuthed) {
      dispatch(getUserThunk());
    }
  }, [dispatch, isAuthed, profile]);

  return (
    <Styles.Div>
      <Styles.Wrapper>
        <Styles.DivTop>
          <Styles.WrapLinks>
            <Styles.NavTopLink to={route.home}>
              {getTranslatedText('header.about')}
            </Styles.NavTopLink>

            <Styles.NavTopLink to={route.home}>
              <Icon.Heart />
              {getTranslatedText('header.goodness')}
            </Styles.NavTopLink>
          </Styles.WrapLinks>

          <Styles.WrapPersonal>
            <Responsive.Desktop>
              <Styles.LoginLink to={isAuthed ? route.userInfo : route.login}>
                <Avatar source={profile?.avatarImage} width={30} height={30} />
              </Styles.LoginLink>
            </Responsive.Desktop>

            <SelectLanguage />
          </Styles.WrapPersonal>
        </Styles.DivTop>
      </Styles.Wrapper>
    </Styles.Div>
  );
};

export default NavTop;
