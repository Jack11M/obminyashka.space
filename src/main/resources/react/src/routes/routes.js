import React from 'react';
import { Route, Switch } from 'react-router-dom';
import { useSelector } from 'react-redux';

import HomePage from '../pages/homepage/HomePage';
import UserInfo from '../pages/UserInfo/UserInfo';
import Auth from '../pages/FormRegister/Auth';
import Protected from './protectedRoute';
import FourOhFourPage from '../pages/ErrorPages/FourOhFourPage';
import ProductPage from '../pages/ProductPage/ProductPage';
import AddGoods from '../pages/AddGoods/AddGoods';
import { route } from './routeConstants';

const Routes = () => {
  const { isAuthed } = useSelector((state) => state.auth);

  return (
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
      <Route path={`${route.productPage}:id`} component={ProductPage} />
      <Route path={route.noMatch} component={FourOhFourPage} />
    </Switch>
  );
};

export default Routes;
