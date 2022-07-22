import Sliders from './slider';
import HeaderInMain from './headerInMain';
import HelpChildren from './helpChildren';
import CurrentOffers from './сurrentOffers';

import { Main, Container } from './styles';

const HomePage = () => (
  <Main>
    <HeaderInMain />

    <Container>
      <CurrentOffers />
      <Sliders />
      <HelpChildren />
    </Container>
  </Main>
);

export default HomePage;
