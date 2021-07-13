import React from 'react';
import { Link } from 'react-router-dom';
import { useSelector } from 'react-redux';
import {
  books,
  clothes,
  furniture,
  kidsUpToYear,
  other,
  shoes,
  toys,
  transportForChildren,
} from 'assets/img/all_images_export/navItems';
import { getTranslatedText } from 'components/local/localisation';
import { route } from 'routes/routeConstants';

import './navCategory.scss';

const NavCategory = () => {
  const { lang } = useSelector((state) => state.auth);
  return (
    <ul className="navbarBurger-list">
      <div className="wrapper">
        <li className="navbarBurger-element navbarBurger-element-clothes">
          <Link to={route.home}>
            <img src={clothes} alt="clothes" />
            <span>{getTranslatedText('header.clothes', lang)}</span>
          </Link>
        </li>

        <li className="navbarBurger-element navbarBurger-element-shoes">
          <Link to={route.home}>
            <img src={shoes} alt="shoes" />
            <span>{getTranslatedText('header.shoes', lang)}</span>
          </Link>
        </li>

        <li className="navbarBurger-element navbarBurger-element-toys">
          <Link to={route.home}>
            <img src={toys} alt="toys" />
            <span>{getTranslatedText('header.toys', lang)}</span>
          </Link>
        </li>

        <li className="navbarBurger-element navbarBurger-element-transportForChildren">
          <Link to={route.home}>
            <img src={transportForChildren} alt="transportForChildren" />
            <span>{getTranslatedText('header.vehicles', lang)}</span>
          </Link>
        </li>

        <li className="navbarBurger-element navbarBurger-element-furniture">
          <Link to={route.home}>
            <img src={furniture} alt="furniture" />
            <span>{getTranslatedText('header.furniture', lang)}</span>
          </Link>
        </li>

        <li className="navbarBurger-element navbarBurger-element-kidsUpToYear">
          <Link to={route.home}>
            <img src={kidsUpToYear} alt="kidsUpToYear" />
            <span>{getTranslatedText('header.babies', lang)}</span>
          </Link>
        </li>

        <li className="navbarBurger-element navbarBurger-element-books">
          <Link to={route.home}>
            <img src={books} alt="books" />
            <span>{getTranslatedText('header.books', lang)}</span>
          </Link>
        </li>

        <li className="navbarBurger-element navbarBurger-element-other">
          <Link to={route.home}>
            <img src={other} alt="other" />
            <span>{getTranslatedText('header.another', lang)}</span>
          </Link>
        </li>
      </div>
    </ul>
  );
};

export default NavCategory;
