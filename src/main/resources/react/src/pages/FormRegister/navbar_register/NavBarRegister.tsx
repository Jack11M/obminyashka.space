import { Outlet } from 'react-router-dom';

import { route } from 'src/routes/routeConstants';
import { getTranslatedText } from 'src/components/local/localization';

import { Tabs } from './styles';
import { CustomLink } from './custom-link';

const NavBarRegister = () => (
  <>
    <Tabs>
      <CustomLink to=''>{getTranslatedText('auth.login')}</CustomLink>
      <CustomLink to={route.signUp}>{getTranslatedText('auth.signUp')}</CustomLink>
    </Tabs>

    <Outlet />
  </>
);

export default NavBarRegister;
