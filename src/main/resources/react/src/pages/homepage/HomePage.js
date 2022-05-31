import Sliders from './slider';
import HeaderInMain from './headerInMain';
import HelpChildren from './helpChildren';
import CurrentOffers from './сurrentOffers';

import './homePage.scss';

const HomePage = () => (
  <main className="Main-page">
    <HeaderInMain />

    <div className="wrapper">
      <CurrentOffers />
      <Sliders />
      <HelpChildren />
    </div>
  </main>
);

export default HomePage;
