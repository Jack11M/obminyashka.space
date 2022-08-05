import { BtnGoodBusiness } from 'components/common';
import { getTranslatedText } from 'components/local/localization';

import * as Styles from './styles';

const HelpChildren = () => {
  return (
    <Styles.HelpChildren>
      <Styles.HelpChildrenTitleH3>
        {getTranslatedText('mainPage.helpTitle')}
      </Styles.HelpChildrenTitleH3>

      <Styles.HelpChildrenText>
        <Styles.Strong>{getTranslatedText('mainPage.helpName')}</Styles.Strong>
        &nbsp;
        {getTranslatedText('mainPage.helpText')}
      </Styles.HelpChildrenText>

      <Styles.StylizedBtn>
        <BtnGoodBusiness
          href="#"
          text={getTranslatedText('mainPage.helpButton')}
        />
      </Styles.StylizedBtn>
    </Styles.HelpChildren>
  );
};
export default HelpChildren;
