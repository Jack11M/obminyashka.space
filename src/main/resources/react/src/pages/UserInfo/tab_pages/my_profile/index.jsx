import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';

import { TitleBigBlue } from 'components/common';
import { getProfile } from 'store/profile/slice';
import { getUserThunk } from 'store/profile/thunk';
import { getTranslatedText } from 'components/local/localization';

import { Children } from './children';
import { AboutMyself } from './about-myself';

const MyProfile = () => {
  const dispatch = useDispatch();
  const profile = useSelector(getProfile);

  useEffect(() => {
    if (!profile) {
      dispatch(getUserThunk());
    }
  }, [dispatch, profile]);

  return (
    <>
      <TitleBigBlue
        style={{ margin: '65px 0 40px' }}
        text={getTranslatedText('ownInfo.aboutMe')}
      />

      <AboutMyself />

      <TitleBigBlue
        style={{ margin: '65px 0 40px' }}
        text={getTranslatedText('ownInfo.children')}
      />

      <Children />
    </>
  );
};
export default React.memo(MyProfile);
