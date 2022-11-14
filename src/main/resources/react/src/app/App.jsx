import { store } from 'store';
import { Provider } from 'react-redux';
import { ThemeProvider } from 'styled-components';
import { BrowserRouter as Router } from 'react-router-dom';

import { theme } from 'styledTheme';
import ScrollToTop from 'components/scrollToTop';
import ErrorBoundary from 'components/errorBoundary';
import { GlobalStyles } from 'styledTheme/globalStyles';
import { ModalProvider, SearchProvider, Toast } from 'components/common';

import Routes from 'routes/routes';
import Header from 'components/header';
import Footer from 'components/footer';

import { Page } from './styles';

const App = () => {
  return (
    <Provider store={store}>
      <ThemeProvider theme={theme}>
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
      </ThemeProvider>
    </Provider>
  );
};
export default App;
