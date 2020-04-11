import React from "react";
import { BrowserRouter as Router } from "react-router-dom";
import ReactDOM from "react-dom";

import Header from "./components/assets/header/Header.jsx";
import Footer from "./components/assets/footer/Footer.jsx";
import Routes from "./routes/routes.jsx";

import "./index.scss";
import "./fonts/fontIcon.scss";

function App() {
  return (
    <Router>
      <Header />
      <Routes />
      <Footer />
    </Router>
  );
}

ReactDOM.render(<App />, document.getElementById("root"));
