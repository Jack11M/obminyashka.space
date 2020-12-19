import React from "react";

import HeaderInMain from "./headerInMain/HeaderInMain";
import Sliders from "./slider/Sliders";
import HelpChildren from "./helpChildren/HelpChildren";
import CurrentOffers from "./сurrentOffers/CurrentOffers";

import "./homePage.scss";

const HomePage = () => {
  return (
    <main className="Main-page">
      <HeaderInMain />
      <div className={"wrapper"}>
        <CurrentOffers />
        <Sliders />
        <HelpChildren />
      </div>
    </main>
  );
};

export default HomePage;
