import { Route, Routes } from 'react-router-dom';

import { route } from 'routes/routeConstants';

import Login from './sign-in';
import SignUp from './sign-up';

const RoutesRegister = () => (
  <Routes>
    <Route path={route.login} element={<Login />} />
    <Route path={route.signUp} element={<SignUp />} />
  </Routes>
);

export default RoutesRegister;
