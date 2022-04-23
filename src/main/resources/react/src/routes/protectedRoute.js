import React from 'react';
import { useSelector } from 'react-redux';
import { Navigate, Route, Routes, useLocation } from 'react-router-dom';

import { route } from './routeConstants';

const Protected = ({ path, Component, permission, redirect }) => {
  return permission ? (
    <Routes>
      <Route path={path} element={Component} />
    </Routes>
  ) : (
    <Navigate to={redirect} />
  );
};
export default Protected;

export const UnauthorizedRoute = ({ children }) => {
  const { isAuthed } = useSelector((state) => state.auth);

  if (isAuthed) {
    return <Navigate to={route.home} />;
  }

  return <>{children}</>;
};

export const AuthorizedRoute = ({ children }) => {
  const { isAuthed } = useSelector((state) => state.auth);
  const location = useLocation();

  if (!isAuthed) {
    return <Navigate to={route.login} state={{ from: location }} />;
  }

  return <>{children}</>;
};
