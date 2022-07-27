import NavTop from './nav_top';
import NavMain from './nav_main';

import { NavContainer } from './styles';

const Header = () => (
  <NavContainer>
    <NavTop />
    <NavMain />
  </NavContainer>
);

export default Header;
