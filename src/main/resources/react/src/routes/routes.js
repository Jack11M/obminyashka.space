import { Route, Routes as Switch } from 'react-router-dom';

import Auth from 'pages/FormRegister/Auth';
import OAuthSuccess from 'pages/OAuthSuccess';
import HomePage from 'pages/homepage/HomePage';
import UserInfo from 'pages/UserInfo/UserInfo';
import AddGoods from 'pages/AddGoods/AddGoods';
import Login from 'pages/FormRegister/sign-in';
import SignUp from 'pages/FormRegister/sign-up';
import { SearchResults } from 'pages/SearchResults';
import ProductPage from 'pages/ProductPage/ProductPage';
import FourOhFourPage from 'pages/ErrorPages/FourOhFourPage';

import { route } from './routeConstants';
import { AuthorizedRoute, UnauthorizedRoute } from './protectedRoute';

const Routes = () => (
  <div>
    <Switch>
      <Route path={route.home} element={<HomePage />} />

      <Route
        path={route.login}
        element={
          <UnauthorizedRoute>
            <Auth />
          </UnauthorizedRoute>
        }
      >
        <Route index element={<Login />} />
        <Route path={route.signUp} element={<SignUp />} />
      </Route>

      <Route
        path={`${route.userInfo}/*`}
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

      <Route path={route.oauthSuccess} element={<OAuthSuccess />} />

      <Route path={route.productPage} element={<ProductPage />} />
      <Route path={route.prevProductPage} element={<ProductPage />} />

      <Route path={route.noMatch} element={<FourOhFourPage />} />

      <Route path={route.SearchResults} element={<SearchResults />} />
    </Switch>
  </div>
);

export default Routes;
