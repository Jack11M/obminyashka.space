import React from 'react';
import { store } from 'store';
import { Provider } from 'react-redux';
import { ThemeProvider } from 'styled-components';
import { BrowserRouter as Router } from 'react-router-dom';

import { theme } from 'styledTheme';
import GlobalFonts from 'styledTheme/styledFonts';
import { ModalProvider } from 'components/common/pop-up';
import ScrollToTop from 'components/scrollToTop/scrollToTop';
import ErrorBoundary from 'components/errorBoundary/errorBoudary';

import Routes from 'routes/routes';
import Header from 'components/header/Header';
import Footer from 'components/footer/Footer';

import { Page } from './styles';

const App = () => {
  return (
    <Provider store={store}>
      <ThemeProvider theme={theme}>
        <GlobalFonts />
        <Router>
          <ErrorBoundary>
            <ScrollToTop />
            <ModalProvider>
              <Page>
                <Header />
                <Routes />
                <Footer />
              </Page>
            </ModalProvider>
          </ErrorBoundary>
        </Router>
      </ThemeProvider>
    </Provider>
  );
};

export default App;
