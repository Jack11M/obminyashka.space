import React from 'react';
import { useSelector } from 'react-redux';
import { Route, Routes as Switch } from 'react-router-dom';

import Auth from 'pages/FormRegister/Auth';
import OAuthSuccess from 'pages/OAuthSuccess';
import HomePage from 'pages/homepage/HomePage';
import UserInfo from 'pages/UserInfo/UserInfo';
import AddGoods from 'pages/AddGoods/AddGoods';
import ProductPage from 'pages/ProductPage/ProductPage';
import FourOhFourPage from 'pages/ErrorPages/FourOhFourPage';
import Login from 'pages/FormRegister/sign-in/Login';
import SignUp from 'pages/FormRegister/sign-up/SignUp';

import { route } from './routeConstants';
import Protected, {
  AuthorizedRoute,
  UnauthorizedRoute,
} from './protectedRoute';

const Routes = () => {
  const { isAuthed } = useSelector((state) => state.auth);

  return (
    <div>
      <Switch>
        <Route path={route.home} element={<HomePage />} />
        {/* <Protected
          path={route.login}
          Component={<Auth />}
          permission={!isAuthed}
          redirect={route.home}
        /> */}

        {/* <Route
          path={route.auth}
          element={
            <UnauthorizedRoute>
              <Auth />
            </UnauthorizedRoute>
          }
        >
          <Route path={route.login} element={<Login />} />
          <Route path={route.signUp} element={<SignUp />} />
        </Route> */}

        {/*
        <Route
          path={`${route.login}${route.signUp}`}
          element={
            <UnauthorizedRoute>
              <SignUp />
            </UnauthorizedRoute>
          }
        /> */}

        <Route
          path={route.userInfo}
          element={
            <AuthorizedRoute>
              <UserInfo />
            </AuthorizedRoute>
          }
        />

        <Route
          path={route.addAdv}
          element={
            <AuthorizedRoute>
              <AddGoods />
            </AuthorizedRoute>
          }
        />

        {/* <Protected
          path={route.userInfo}
          Component={UserInfo}
          permission={isAuthed}
          redirect={route.login}
        /> */}

        {/* <Protected
          path={route.addAdv}
          Component={AddGoods}
          permission={isAuthed}
          redirect={route.login}
        /> */}

        <Route path={route.oauthSuccess} element={<OAuthSuccess />} />

        <Route path={route.productPage} element={<ProductPage />} />

        <Route path={route.login} element={<Auth />}>
          <Route index element={<Login />} />
          <Route path={route.signUp} element={<SignUp />} />
        </Route>

        <Route path={route.noMatch} element={<FourOhFourPage />} />
      </Switch>
    </div>
  );
};

export default Routes;
