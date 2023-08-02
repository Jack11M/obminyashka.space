import { Provider } from "react-redux";
import { ThemeWrap, Toast, Footer } from "obminyashka-components";
import { BrowserRouter as Router } from "react-router-dom";

import { store } from "src/store";
import { route } from "src/routes/routeConstants";
import ScrollToTop from "src/components/scrollToTop";
import ErrorBoundary from "src/components/errorBoundary";
import { GlobalStyles } from "src/styledTheme/globalStyles";
import { getTranslatedText } from "src/components/local/localization";
import { ModalProvider, SearchProvider } from "src/components/common";

import Routes from "src/routes/routes";
import { Header } from "src/components/header";

import { Page } from "./styles";

const App = () => {
  return (
    <Provider store={store}>
      <ThemeWrap>
        <GlobalStyles />
        <Router>
          <ErrorBoundary>
            <ScrollToTop />
            <ModalProvider>
              <SearchProvider>
                <Page>
                  <Header />
                  <Routes />
                  <Footer
                    toMain={route.home}
                    toDeals={route.home}
                    toRules={route.home}
                    toCharity={route.home}
                    toQuestions={route.home}
                    tel1={"+3 80 (93) 123 45 67"}
                    tel2={"+3 80 (93) 123 45 67"}
                    email={"obminyashka.space@gmail.com"}
                    rules={getTranslatedText("footer.rules")}
                    text={getTranslatedText("header.goodness")}
                    charity={getTranslatedText("footer.charity")}
                    name={getTranslatedText("mainPage.helpName")}
                    protect={getTranslatedText("footer.protect")}
                    questions={getTranslatedText("footer.questions")}
                  />
                  <Toast limit={5} />
                </Page>
              </SearchProvider>
            </ModalProvider>
          </ErrorBoundary>
        </Router>
      </ThemeWrap>
    </Provider>
  );
};

export { App };
