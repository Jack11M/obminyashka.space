import React from "react";

import "./homePage.scss";
import HeaderInMain from "./headerInMain/HeaderInMain";
import Sliders from "./slider/Sliders";
import HelpChildren from "./helpChildren/HelpChildren";
import CurrentOffers from "./сurrentOffers/CurrentOffers";

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
