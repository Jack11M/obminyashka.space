import React from 'react';
import { Title } from '@wolshebnik/obminyashka-components';

import { getTranslatedText } from 'components/local/localization';

import { Children } from './children';
import { AboutMyself } from './about-myself';

const MyProfile = () => {
  return (
    <>
      <Title
        style={{ margin: '65px 0 40px' }}
        text={getTranslatedText('ownInfo.aboutMe')}
      />

      <AboutMyself />

      <Title
        style={{ margin: '65px 0 40px' }}
        text={getTranslatedText('ownInfo.children')}
      />

      <Children />
    </>
  );
};
export default React.memo(MyProfile);
