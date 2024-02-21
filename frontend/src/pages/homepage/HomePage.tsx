import { HelpChildren, SitePurpose, Swiper } from "obminyashka-components";

import { route } from "src/routes/routeConstants";
import { getTranslatedText } from "src/components/local";

import { slidesData } from "./mock";
import { CurrentOffers } from "./currentOffers";

import "swiper/css";
import * as Styles from "./styles";

const HomePage = () => {
  return (
    <Styles.Main>
      <SitePurpose
        regTextRoute={route.addAdv}
        tradeTextRoute={route.login}
        thingsTextRoute={route.addAdv}
        regText={getTranslatedText("mainAnimationText.regText")}
        tradeText={getTranslatedText("mainAnimationText.tradeText")}
        thingsText={getTranslatedText("mainAnimationText.thingsText")}
        regTextTwo={getTranslatedText("mainAnimationText.regTextTwo")}
        regTextLink={getTranslatedText("mainAnimationText.regTextLink")}
        tradeTextLink={getTranslatedText("mainAnimationText.tradeTextLink")}
        thingsTextLink={getTranslatedText("mainAnimationText.thingsTextLink")}
      />

      <Styles.Container>
        <CurrentOffers />

        <Swiper data={slidesData} />

        <HelpChildren
          name={getTranslatedText("mainPage.helpName")}
          text={getTranslatedText("mainPage.helpText")}
          title={getTranslatedText("mainPage.helpTitle")}
          buttonText={getTranslatedText("mainPage.helpButton")}
        />
      </Styles.Container>
    </Styles.Main>
  );
};

export default HomePage;
