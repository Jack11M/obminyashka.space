import { useSelector } from 'react-redux';

import { getLang } from 'store/auth/slice';
import { BtnGoodBusiness } from 'components/common/buttons';
import { getTranslatedText } from 'components/local/localization';

import * as Styles from './styles';

const HelpChildren = () => {
  const lang = useSelector(getLang);

  return (
    <Styles.HelpChildren>
      <Styles.HelpChildrenTitleH3>
        {getTranslatedText('mainPage.helpTitle', lang)}
      </Styles.HelpChildrenTitleH3>

      <Styles.HelpChildrenText>
        <Styles.Strong>
          {getTranslatedText('mainPage.helpName', lang)}
        </Styles.Strong>
        &nbsp;
        {getTranslatedText('mainPage.helpText', lang)}
      </Styles.HelpChildrenText>

      <Styles.StylizedBtn>
        <BtnGoodBusiness
          href="#"
          text={getTranslatedText('mainPage.helpButton', lang)}
        />
      </Styles.StylizedBtn>
    </Styles.HelpChildren>
  );
};
export default HelpChildren;
