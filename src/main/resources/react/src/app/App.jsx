import { store } from 'store';
import { Provider } from 'react-redux';
import { ThemeWrap, Toast } from 'obminyashka-components';
import { BrowserRouter as Router } from 'react-router-dom';

import ScrollToTop from 'components/scrollToTop';
import ErrorBoundary from 'components/errorBoundary';
import { GlobalStyles } from 'styledTheme/globalStyles';
import { ModalProvider, SearchProvider } from 'components/common';

import Routes from 'routes/routes';
import Footer from 'components/footer';
import Header from 'components/header';

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
export default App;
