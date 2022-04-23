import NavBarRegister from './navbar_register/NavBarRegister';

import cls from './Auth.module.scss';

const Auth = () => {
  return (
    <div className={cls.popup}>
      <NavBarRegister />
    </div>
  );
};

export default Auth;
