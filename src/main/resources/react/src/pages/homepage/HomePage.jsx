import { HelpChildren } from 'obminyashka-components';

import { getTranslatedText } from 'components/local';

import Slides from './slider';
import HeaderInMain from './headerInMain';
import { CurrentOffers } from './currentOffers';

import * as Styles from './styles';

const HomePage = () => {
  return (
    <Styles.Main>
      <HeaderInMain />

      <Styles.Container>
        <CurrentOffers />
        <Slides />

        <HelpChildren
          name={getTranslatedText('mainPage.helpName')}
          text={getTranslatedText('mainPage.helpText')}
          title={getTranslatedText('mainPage.helpTitle')}
          buttonText={getTranslatedText('mainPage.helpButton')}
        />
      </Styles.Container>
    </Styles.Main>
  );
};

export default HomePage;
