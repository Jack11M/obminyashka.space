import { store } from 'store';
import { useContext } from 'react';
import { Provider } from 'react-redux';
import { ThemeProvider } from 'styled-components';
import { BrowserRouter as Router } from 'react-router-dom';
import {
  Toast,
  ThemeWrap,
  ThemeContext,
} from '@wolshebnik/obminyashka-components';

import ScrollToTop from 'components/scrollToTop';
import ErrorBoundary from 'components/errorBoundary';
import { GlobalStyles } from 'styledTheme/globalStyles';
import { ModalProvider, SearchProvider } from 'components/common';

import Routes from 'routes/routes';
import Header from 'components/header';
import Footer from 'components/footer';

import { Page } from './styles';

const App = () => {
  const { theme } = useContext(ThemeContext);
  return (
    <Provider store={store}>
      <ThemeProvider theme={theme}>
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
                    <Toast />
                  </Page>
                </SearchProvider>
              </ModalProvider>
            </ErrorBoundary>
          </Router>
        </ThemeWrap>
      </ThemeProvider>
    </Provider>
  );
};
export default App;
