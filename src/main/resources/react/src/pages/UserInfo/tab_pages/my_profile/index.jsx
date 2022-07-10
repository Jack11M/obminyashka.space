import React, { useEffect } from 'react';
import { useDispatch } from 'react-redux';

import { TitleBigBlue } from 'components/common';
import { getUserThunk } from 'store/profile/thunk';
import { getTranslatedText } from 'components/local/localization';

import { Children } from './children';
import { AboutMyself } from './about-myself';

import './myProfile.scss';

const MyProfile = () => {
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(getUserThunk());
  }, [dispatch]);

  return (
    <>
      <TitleBigBlue
        whatClass="myProfile-title"
        text={getTranslatedText('ownInfo.aboutMe')}
      />

      <AboutMyself />

      <TitleBigBlue
        whatClass="myProfile-title"
        text={getTranslatedText('ownInfo.children')}
      />

      <Children />
    </>
  );
};
export default React.memo(MyProfile);
