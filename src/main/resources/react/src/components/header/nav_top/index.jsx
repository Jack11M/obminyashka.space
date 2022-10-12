import { useEffect, useState } from 'react';
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

const string = 'data:image/jpeg;base64,';

const NavTop = () => {
  const dispatch = useDispatch();
  const isAuthed = useSelector(getAuthed);
  const profile = useSelector(getAuthProfile);
  const { avatarImage } = useSelector(getProfile);

  const [image, setImage] = useState('');

  useEffect(() => {
    if (!avatarImage && isAuthed) {
      dispatch(getUserThunk());
    }
  }, [dispatch, isAuthed, avatarImage]);

  useEffect(() => {
    if (!avatarImage?.includes(string) && avatarImage) {
      setImage(`${string}${avatarImage}`);
    }

    if (avatarImage?.includes(string) && avatarImage) {
      setImage(avatarImage);
    }

    if (!avatarImage) setImage(null);
  }, [avatarImage]);

  return (
    <Styles.Div>
      <Styles.Wrapper>
        <Styles.DivTop>
          <Styles.WrapLinks>
            <Styles.NavTopLink to="/">
              {getTranslatedText('header.about')}
            </Styles.NavTopLink>

            <Styles.NavTopLink to={route.home}>
              <HeartSvg />
              {getTranslatedText('header.goodness')}
            </Styles.NavTopLink>
          </Styles.WrapLinks>

          <Styles.WrapPersonal>
            <Styles.LoginLink to={isAuthed ? route.userInfo : route.login}>
              <Avatar source={image} />

              <Styles.ProfileSpan>
                <EllipsisText>
                  {profile?.username || getTranslatedText('header.myOffice')}
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
