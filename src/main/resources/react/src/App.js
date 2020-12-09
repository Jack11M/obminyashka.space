import React from 'react';
import { BrowserRouter as Router } from 'react-router-dom';
import { ThemeProvider } from 'styled-components';

import ScrollToTop from './components/scrollToTop/scrollToTop';
import ErrorBoundary from './components/errorBoundary/errorBoudary';
import Header from './components/header/Header';
import Routes from './routes/routes';
import Footer from './components/footer/Footer';

import { theme } from './styledTheme';
import GlobalFonts from './styledTheme/styledFonts';

const App = () => {
	return (
		<ThemeProvider theme={ theme }>
			<GlobalFonts/>
			<Router>
				<ErrorBoundary>
					<ScrollToTop/>
					<Header/>
					<Routes/>
					<Footer/>
				</ErrorBoundary>
			</Router>
		</ThemeProvider>
	);
};

export default App;
