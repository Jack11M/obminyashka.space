import { BtnGoodBusiness } from 'components/common/buttons';
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
      whatClass="HelpChildren__btn "
      text={getTranslatedText('mainPage.helpButton')}
      href="#"
    />
  </div>
);

export default HelpChildren;
