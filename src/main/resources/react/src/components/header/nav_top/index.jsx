import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';

import { route } from 'routes/routeConstants';
import { EllipsisText } from 'components/common';
import { getProfile } from 'store/profile/slice';
import { Avatar } from 'components/common/avatar';
import { getUserThunk } from 'store/profile/thunk';
import { CustomSelect } from 'components/selectLang';
import { getAuthed, getAuthProfile } from 'store/auth/slice';
import { getTranslatedText } from 'components/local/localization';
import { ReactComponent as HeartSvg } from 'assets/icons/heart.svg';

import * as Styles from './styles';

const NavTop = () => {
  const dispatch = useDispatch();
  const isAuthed = useSelector(getAuthed);
  const authProfile = useSelector(getAuthProfile);
  const profile = useSelector(getProfile);

  useEffect(() => {
    if (!profile?.avatarImage && isAuthed) {
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
              <HeartSvg />
              {getTranslatedText('header.goodness')}
            </Styles.NavTopLink>
          </Styles.WrapLinks>

          <Styles.WrapPersonal>
            <Styles.LoginLink to={isAuthed ? route.userInfo : route.login}>
              <Avatar source={profile?.avatarImage} />

              <Styles.ProfileSpan>
                <EllipsisText>
                  {authProfile?.username ||
                    getTranslatedText('header.myOffice')}
                </EllipsisText>
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
