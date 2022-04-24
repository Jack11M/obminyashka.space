import React from 'react';
import { useSelector } from 'react-redux';

import { getLang } from 'store/auth/slice';
import { getTranslatedText } from 'components/local/localization';
import BtnGoodBusiness from 'components/common/buttons/btnGoodBusiness/BtnGoodBusiness';

import './helpchildren.scss';

const HelpChildren = () => {
  const lang = useSelector(getLang);

  return (
    <div className="HelpChildren">
      <h3 className="HelpChildren__title">
        {getTranslatedText('mainPage.helpTitle', lang)}
      </h3>
      <p className="HelpChildren__text">
        <strong>{getTranslatedText('mainPage.helpName', lang)}</strong>
        &nbsp; {getTranslatedText('mainPage.helpText', lang)}
      </p>
      <BtnGoodBusiness
        whatClass={'HelpChildren__btn '}
        text={getTranslatedText('mainPage.helpButton', lang)}
        href={'#'}
      />
    </div>
  );
};

export default HelpChildren;
