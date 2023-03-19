import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Avatar, Icon, EllipsisText } from 'obminyashka-components';

import { route } from 'routes/routeConstants';
import { getProfile } from 'store/profile/slice';
import { getUserThunk } from 'store/profile/thunk';
import { SelectLanguage } from 'components/selectLang';
import { getAuthed, getAuthProfile } from 'store/auth/slice';
import { getTranslatedText } from 'components/local/localization';

import * as Styles from './styles';

const NavTop = () => {
  const dispatch = useDispatch();
  const isAuthed = useSelector(getAuthed);
  const authProfile = useSelector(getAuthProfile);
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
            <Styles.LoginLink to={isAuthed ? route.userInfo : route.login}>
              <Avatar source={profile?.avatarImage} />

              <Styles.ProfileSpan>
                <EllipsisText
                  width={400}
                  offset={50}
                  place="left"
                  id="show-tooltip"
                >
                  {authProfile?.username ||
                    getTranslatedText('header.myOffice')}
                </EllipsisText>
              </Styles.ProfileSpan>
            </Styles.LoginLink>

            <SelectLanguage />
          </Styles.WrapPersonal>
        </Styles.DivTop>
      </Styles.Wrapper>
    </Styles.Div>
  );
};

export default NavTop;
