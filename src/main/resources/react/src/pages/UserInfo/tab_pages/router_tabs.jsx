import { useEffect } from 'react';
import { Route, Routes, useLocation } from 'react-router-dom';

import { route } from 'routes/routeConstants';

import MyProfile from './my_profile';
import MyActivity from './my_activity';
import MySettings from './my_settings';
import MyFavorites from './my_favorites';

const RouterTabs = ({ set }) => {
  const location = useLocation();
  const [prevLocation, setPrevLocation] = set;

  useEffect(
    () => () => setPrevLocation(location.pathname),
    [setPrevLocation, location]
  );

  const findComponentForExit = () => {
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
  };

  return (
    <Routes>
      <Route index element={<MyActivity />} />
      <Route path={route.myProfile} element={<MyProfile />} />
      <Route path={route.myFavorite} element={<MyFavorites />} />
      <Route path={route.mySettings} element={<MySettings />} />
      <Route path={route.exit} element={findComponentForExit()} />
    </Routes>
  );
};

export { RouterTabs };
