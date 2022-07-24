import Sliders from './slider';
import HeaderInMain from './headerInMain';
import HelpChildren from './helpChildren';
import CurrentOffers from './сurrentOffers';

import * as Styles from './styles';

const HomePage = () => (
  <Styles.Main>
    <HeaderInMain />

    <Styles.Container>
      <CurrentOffers />
      <Sliders />
      <HelpChildren />
    </Styles.Container>
  </Styles.Main>
);

export default HomePage;
