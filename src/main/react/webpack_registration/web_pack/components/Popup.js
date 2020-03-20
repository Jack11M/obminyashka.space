import React from "react";
import { BrowserRouter as Router } from "react-router-dom";
import NavBar from "./navbar/NavBar";
import Routes from "./pages/routes";


const Popup = () => {
  return (
    <div className={"popup"}>
      <Router>
        <NavBar />
        <Routes />
      </Router>
    </div>
  );
};

export default Popup;
