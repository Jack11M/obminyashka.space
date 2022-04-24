import React from 'react';
import { useSelector } from 'react-redux';
import { Navigate, useLocation } from 'react-router-dom';

import { getAuth } from 'store/auth/slice';

import { route } from './routeConstants';

export const UnauthorizedRoute = ({ children }) => {
  const isAuth = useSelector(getAuth);

  if (isAuth) {
    return <Navigate to={route.home} />;
  }

  return <>{children}</>;
};

export const AuthorizedRoute = ({ children }) => {
  const isAuth = useSelector(getAuth);
  const location = useLocation();

  if (!isAuth) {
    return <Navigate to={route.login} state={{ from: location }} />;
  }

  return <>{children}</>;
};
