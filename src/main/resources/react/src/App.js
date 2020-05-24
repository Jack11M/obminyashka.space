import React from "react";
import Header from "./components/header/Header";
import Routes from "./routes/routes";
import Footer from "./components/footer/Footer";
import { BrowserRouter as Router } from "react-router-dom";
import ScrollToTop from "./components/scrollToTop/scrollToTop";
import ErrorBoundary from "./components/errorBoundary/errorBoudary";

const App = () => {
  return (
    <Router>
      <ErrorBoundary>
        <ScrollToTop />
        <Header />
        <Routes />
        <Footer />
      </ErrorBoundary>
    </Router>
  );
};

export default App;
