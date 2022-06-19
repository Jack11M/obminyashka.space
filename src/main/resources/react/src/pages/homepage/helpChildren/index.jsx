import { BtnGoodBusiness } from 'components/common';
import { getTranslatedText } from 'components/local/localization';

import './helpchildren.scss';

const HelpChildren = () => (
  <div className="HelpChildren">
    <h3 className="HelpChildren__title">
      {getTranslatedText('mainPage.helpTitle')}
    </h3>

    <p className="HelpChildren__text">
      <strong>{getTranslatedText('mainPage.helpName')}</strong>
      &nbsp;
      {getTranslatedText('mainPage.helpText')}
    </p>

    <BtnGoodBusiness
      href="#"
      whatClass="HelpChildren__btn "
      text={getTranslatedText('mainPage.helpButton')}
    />
  </div>
);

export default HelpChildren;
