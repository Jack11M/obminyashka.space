import React, { useCallback, useEffect } from 'react';
import { Navigate, Route, Routes, useLocation } from 'react-router-dom';

import MyActivity from './my_activity';
import MyProfile from './my_profile';
import MyFavorites from './my_favorites';
import MySettings from './my_settings';
import { route } from 'routes/routeConstants';

const RouterTabs = ({ url, set }) => {
  const location = useLocation();
  const [prevLocation, setPrevLocation] = set;

  useEffect(() => {
    return () => setPrevLocation(location.pathname);
  }, [setPrevLocation, location]);

  const findComponentForExit = useCallback(() => {
    let LastComponent = MyActivity;
    switch (prevLocation) {
      case route.userInfo:
        LastComponent = MyActivity;
        return <MyActivity />;
      case `${route.userInfo}${route.myProfile}`:
        LastComponent = MyProfile;
        return <MyProfile />;
      case `${route.userInfo}${route.myFavorite}`:
        LastComponent = MyFavorites;
        return <MyFavorites />;
      case `${route.userInfo}${route.mySettings}`:
        LastComponent = MySettings;
        return <MySettings />;
      default:
        return <LastComponent />;
    }
  }, [prevLocation]);

  return (
    <Routes>
      <Route path={`${url}`} element={<MyActivity />} exact />
      <Route path={`${url}${route.myProfile}`} element={<MyProfile />} />
      <Route path={`${url}${route.myFavorite}`} element={<MyFavorites />} />
      <Route path={`${url}${route.mySettings}`} element={<MySettings />} />
      <Route path={`${url}${route.exit}`} element={findComponentForExit()} />
      <Navigate to={`${url}`} />
    </Routes>
  );
};

export default RouterTabs;
