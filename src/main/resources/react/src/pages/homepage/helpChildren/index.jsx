import { useSelector } from 'react-redux';

import { getLang } from 'store/auth/slice';
import { BtnGoodBusiness } from 'components/common/buttons';
import { getTranslatedText } from 'components/local/localization';

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
        &nbsp;
        {getTranslatedText('mainPage.helpText', lang)}
      </p>
      <BtnGoodBusiness
        whatClass="HelpChildren__btn "
        text={getTranslatedText('mainPage.helpButton', lang)}
        href="#"
      />
    </div>
  );
};

export default HelpChildren;
