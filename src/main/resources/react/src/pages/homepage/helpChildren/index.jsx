import { Button, Icon, Title } from 'obminyashka-components';

import { getTranslatedText } from 'components/local/localization';

import * as Styles from './styles';

const HelpChildren = () => {
  return (
    <Styles.HelpChildren>
      <Styles.TitleWrapper>
        <Title hiddenDots text={getTranslatedText('mainPage.helpTitle')} />
      </Styles.TitleWrapper>

      <Styles.HelpChildrenText>
        <Styles.Strong>{getTranslatedText('mainPage.helpName')}</Styles.Strong>
        &nbsp;
        {getTranslatedText('mainPage.helpText')}
      </Styles.HelpChildrenText>

      <Button
        width={357}
        icon={<Icon.Heart />}
        text={getTranslatedText('mainPage.helpButton')}
      />
    </Styles.HelpChildren>
  );
};

export default HelpChildren;
