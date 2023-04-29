import { useSelector } from 'react-redux';
import { Navigate, useLocation } from 'react-router-dom';

import { getAuthed } from 'src/store/auth/slice';

import { route } from './routeConstants';

export const UnauthorizedRoute = ({ children }) => {
  const isAuth = useSelector(getAuthed);

  if (isAuth) return <Navigate to={route.home} />;

  return children;
};

export const AuthorizedRoute = ({ children }) => {
  const isAuth = useSelector(getAuthed);
  const location = useLocation();

  if (!isAuth) {
    return <Navigate to={route.login} state={{ from: location }} />;
  }

  return children;
};
