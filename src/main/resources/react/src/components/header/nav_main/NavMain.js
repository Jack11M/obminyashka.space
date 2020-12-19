import React from "react";
import { Link } from "react-router-dom";
import ButtonAdv from "../../buttonAdv/ButtonAdv.js";
import NavCategory from "../nav_category/NavCategory.js";
import { getTranslatedText } from '../../local/localisation';

import "./navMain.scss";

const NavMain = () => {
  return (
    <div className="navbar-main-inner">
      <div className="wrapper">
        <div className="navbar-main">
          <Link to="/" className="logo"/>
          <div className="navbar-main__select">
            <div className={"navbar-main__select-top"}>{ getTranslatedText('header.categories') }</div>
            <div className={"navbar-main__select-bottom"}>{ getTranslatedText('header.categories') }</div>
            <NavCategory />
          </div>
          <div className="navbar-main-search">
            <input type="text" id="search" placeholder={ `${getTranslatedText('header.iSearch')} ...` } />
            <label htmlFor="search" className="circle">
              <label htmlFor="search" className="icon-search"/>
            </label>
          </div>
          <ButtonAdv />
        </div>
      </div>
    </div>
  );
};

export default NavMain;
