import { Link } from 'react-router-dom';

import { route } from 'routes/routeConstants';
import {
  toys,
  books,
  other,
  shoes,
  clothes,
  furniture,
  kidsUpToYear,
  transportForChildren,
} from 'assets/img/all_images_export/navItems';
import { getTranslatedText } from 'components/local/localization';

import './navCategory.scss';

const NavCategory = () => (
  <ul className="navbarBurger-list">
    <div className="wrapper">
      <li className="navbarBurger-element navbarBurger-element-clothes">
        <Link to={route.home}>
          <img src={clothes} alt="clothes" />
          <span>{getTranslatedText('header.clothes')}</span>
        </Link>
      </li>

      <li className="navbarBurger-element navbarBurger-element-shoes">
        <Link to={route.home}>
          <img src={shoes} alt="shoes" />
          <span>{getTranslatedText('header.shoes')}</span>
        </Link>
      </li>

      <li className="navbarBurger-element navbarBurger-element-toys">
        <Link to={route.home}>
          <img src={toys} alt="toys" />
          <span>{getTranslatedText('header.toys')}</span>
        </Link>
      </li>

      <li className="navbarBurger-element navbarBurger-element-transportForChildren">
        <Link to={route.home}>
          <img src={transportForChildren} alt="transportForChildren" />
          <span>{getTranslatedText('header.vehicles')}</span>
        </Link>
      </li>

      <li className="navbarBurger-element navbarBurger-element-furniture">
        <Link to={route.home}>
          <img src={furniture} alt="furniture" />
          <span>{getTranslatedText('header.furniture')}</span>
        </Link>
      </li>

      <li className="navbarBurger-element navbarBurger-element-kidsUpToYear">
        <Link to={route.home}>
          <img src={kidsUpToYear} alt="kidsUpToYear" />
          <span>{getTranslatedText('header.babies')}</span>
        </Link>
      </li>

      <li className="navbarBurger-element navbarBurger-element-books">
        <Link to={route.home}>
          <img src={books} alt="books" />
          <span>{getTranslatedText('header.books')}</span>
        </Link>
      </li>

      <li className="navbarBurger-element navbarBurger-element-other">
        <Link to={route.home}>
          <img src={other} alt="other" />
          <span>{getTranslatedText('header.another')}</span>
        </Link>
      </li>
    </div>
  </ul>
);

export default NavCategory;
