import React from "react";
import Header from "./components/header/Header";
import Routes from "./routes/routes";
import Footer from "./components/footer/Footer";
import { BrowserRouter as Router } from "react-router-dom";

const App = () => {
  return (
    <Router>
      <Header />
      <Routes />
      <Footer />
    </Router>
  );
};

export default App;
