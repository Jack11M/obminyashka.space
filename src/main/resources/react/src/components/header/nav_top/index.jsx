import { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';

import { route } from 'routes/routeConstants';
import { getProfile } from 'store/profile/slice';
import { Avatar } from 'components/common/avatar';
import { CustomSelect } from 'components/selectLang';
import { getAuth, getAuthProfile } from 'store/auth/slice';
import { getTranslatedText } from 'components/local/localization';
import { ReactComponent as HeartSvg } from 'assets/icons/heart.svg';

import * as Styles from './styles';

const NavTop = () => {
  const isAuthed = useSelector(getAuth);
  const profile = useSelector(getAuthProfile);
  const { avatarImage } = useSelector(getProfile);

  const [image, setImage] = useState('');

  useEffect(() => {
    if (!avatarImage?.includes('blob') && avatarImage !== '') {
      setImage(`data:image/jpeg;base64,${avatarImage}`);
    }

    if (avatarImage?.includes('blob')) {
      setImage(avatarImage);
    }

    if (avatarImage === '') setImage('');
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
                {profile?.username || getTranslatedText('header.myOffice')}
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
