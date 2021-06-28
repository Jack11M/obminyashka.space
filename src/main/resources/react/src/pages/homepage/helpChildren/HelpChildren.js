import React from 'react';
import BtnGoodBusiness from '../../../components/common/btnGoodBusiness/BtnGoodBusiness.js';
import './helpchildren.scss';
import { useSelector } from 'react-redux';
import { getTranslatedText } from '../../../components/local/localisation';

const HelpChildren = () => {
  const { lang } = useSelector((state) => state.auth);

  return (
    <div className="HelpChildren">
      <h3 className="HelpChildren__title">
        {getTranslatedText('mainPage.helpTitle', lang)}
      </h3>
      <p className="HelpChildren__text">
        <strong>{getTranslatedText('mainPage.helpName', lang)}</strong>
        {getTranslatedText('mainPage.helpText', lang)}
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
