import React from 'react';
import { Navigate, Route, Routes, useLocation } from 'react-router-dom';

import { route } from 'routes/routeConstants';

import Login from './sign-in/Login';
import SignUp from './sign-up/SignUp';

const RoutesRegister = () => {
  const location = useLocation();
  console.log(location);
  return (
    <Routes>
      <Route path={route.login} element={<Login />} />
      <Route path={route.signUp} element={<SignUp />} />
    </Routes>
  );
};

export default RoutesRegister;
