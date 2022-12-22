import React from 'react';

import { TitleBigBlue } from 'components/common';
import { getTranslatedText } from 'components/local/localization';

import { Children } from './children';
import { AboutMyself } from './about-myself';

const MyProfile = () => {
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
