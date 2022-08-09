import { Outlet } from 'react-router-dom';

import { route } from 'routes/routeConstants';
import { getTranslatedText } from 'components/local/localization';

import { CustomLink } from './custom-link';
import cls from './registerTabs.module.scss';

const NavBarRegister = () => (
  <>
    <div className={cls.tabs}>
      <CustomLink to="">{getTranslatedText('auth.login')}</CustomLink>
      <CustomLink to={route.signUp}>
        {getTranslatedText('auth.signUp')}
      </CustomLink>
    </div>

    <Outlet />
  </>
);

export default NavBarRegister;
