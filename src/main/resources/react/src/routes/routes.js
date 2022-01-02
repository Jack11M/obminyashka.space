import React from 'react';
import { useSelector } from 'react-redux';
import { Route, Switch } from 'react-router-dom';

import Auth from 'pages/FormRegister/Auth';
import OAuthSuccess from 'pages/OAuthSuccess';
import HomePage from 'pages/homepage/HomePage';
import UserInfo from 'pages/UserInfo/UserInfo';
import AddGoods from 'pages/AddGoods/AddGoods';
import ProductPage from 'pages/ProductPage/ProductPage';
import FourOhFourPage from 'pages/ErrorPages/FourOhFourPage';

import { route } from './routeConstants';
import Protected from './protectedRoute';

const Routes = () => {
  const { isAuthed } = useSelector((state) => state.auth);

  return (
    <div>
      <Switch>
        <Route path={route.home} component={HomePage} exact />
        <Protected
          path={route.login}
          component={Auth}
          permission={!isAuthed}
          redirect={route.home}
        />

        <Protected
          path={route.userInfo}
          component={UserInfo}
          permission={isAuthed}
          redirect={route.login}
        />

        <Protected
          path={route.addAdv}
          component={AddGoods}
          permission={isAuthed}
          redirect={route.login}
        />

        <Route path={route.oauthSuccess} component={OAuthSuccess} />

        <Route path={`${route.productPage}`} component={ProductPage} />

        <Route path={route.noMatch} component={FourOhFourPage} />
      </Switch>
    </div>
  );
};

export default Routes;
