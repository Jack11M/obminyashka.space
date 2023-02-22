import { Button } from '@wolshebnik/obminyashka-components';

import * as Icon from 'assets/icons';
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

      <Button
        width={357}
        icon={<Icon.HeartSvg />}
        text={getTranslatedText('mainPage.helpButton')}
      />
    </Styles.HelpChildren>
  );
};
export default HelpChildren;
