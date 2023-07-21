import { Provider } from 'react-redux';
import { ThemeWrap, Toast } from 'obminyashka-components';
import { BrowserRouter as Router } from 'react-router-dom';

import { store } from 'src/store';
import ScrollToTop from 'src/components/scrollToTop';
import ErrorBoundary from 'src/components/errorBoundary';
import { GlobalStyles } from 'src/styledTheme/globalStyles';
import { ModalProvider, SearchProvider } from 'src/components/common';

import Routes from 'src/routes/routes';
import Footer from 'src/components/footer';
import { Header } from 'src/components/header';

import { Page } from './styles';

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
                  <Footer />
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
