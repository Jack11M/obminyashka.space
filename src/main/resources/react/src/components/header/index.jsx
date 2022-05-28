import NavTop from './nav_top';
import NavMain from './nav_main';

import './Navbar.scss';

const Header = () => (
  <header className="navbar">
    <NavTop />
    <NavMain />
  </header>
);

export default Header;
