import React from "react";
import { Link } from 'react-router-dom';
import "./navCategory.scss";
import {
  clothes,
  shoes,
  toys,
  transportForChildren,
  furniture,
  kidsUpToYear,
  books,
  other
} from "../../../img/all_images_export/navItems";

const NavCategory = () => {
  return (
    <ul className="navbarBurger-list">
      <div className="wrapper">
        <li className="navbarBurger-element navbarBurger-element-clothes">
          <Link to={'/'}>
            <img src={clothes} alt="clothes" />
            <span>одежда</span>
          </Link>
        </li>

        <li className="navbarBurger-element navbarBurger-element-shoes">
          <Link to={'/'}>
            <img src={shoes} alt="shoes" />
            <span>обувь</span>
          </Link>
        </li>

        <li className="navbarBurger-element navbarBurger-element-toys">
          <Link to={'/'}>
            <img src={toys} alt="toys" />
            <span>игрушки</span>
          </Link>
        </li>

        <li className="navbarBurger-element navbarBurger-element-transportForChildren">
          <Link to={'/'}>
            <img src={transportForChildren} alt="transportForChildren" />
            <span>транспорт для детей</span>
          </Link>
        </li>

        <li className="navbarBurger-element navbarBurger-element-furniture">
          <Link to={'/'}>
            <img src={furniture} alt="furniture" />
            <span>детская мебель</span>
          </Link>
        </li>

        <li className="navbarBurger-element navbarBurger-element-kidsUpToYear">
          <Link to={'/'}>
            <img src={kidsUpToYear} alt="kidsUpToYear" />
            <span>малыши до года</span>
          </Link>
        </li>

        <li className="navbarBurger-element navbarBurger-element-books">
          <Link to={'/'}>
            <img src={books} alt="books" />
            <span>книги</span>
          </Link>
        </li>

        <li className="navbarBurger-element navbarBurger-element-other">
          <Link to={'/'}>
            <img src={other} alt="other" />
            <span>другое</span>
          </Link>
        </li>
      </div>
    </ul>
  );
};

export default NavCategory;
