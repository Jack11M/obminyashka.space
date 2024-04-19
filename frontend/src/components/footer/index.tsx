import { useLocation } from "react-router-dom";
import { Footer as FooterComponent } from "obminyashka-components";

import { route } from "src/routes/routeConstants";

import * as Styles from "./style.ts";
import { isWhiteBG } from "./helpers.ts";
import { getTranslatedText } from "../local/index.ts";

const Footer = () => {
  const { pathname } = useLocation();
  const isSearchResults = pathname?.replace("/", "") === route.SearchResults;

  return (
    <Styles.Wrapper
      withBg={isWhiteBG(pathname)}
      isSearchResults={isSearchResults}
    >
      <FooterComponent
        toMain={route.home}
        toDeals={route.home}
        toRules={route.home}
        toCharity={route.home}
        toQuestions={route.home}
        tel1={"+3 80 (93) 123 45 67"}
        tel2={"+3 80 (93) 123 45 67"}
        inFooterOAuth={isWhiteBG(pathname)}
        email={"obminyashka.space@gmail.com"}
        rules={getTranslatedText("footer.rules")}
        text={getTranslatedText("header.goodness")}
        charity={getTranslatedText("footer.charity")}
        name={getTranslatedText("mainPage.helpName")}
        protect={getTranslatedText("footer.protect")}
        questions={getTranslatedText("footer.questions")}
      />
    </Styles.Wrapper>
  );
};

export { Footer };
