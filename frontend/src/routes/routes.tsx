import { Route, Routes as Switch } from "react-router-dom";

import { Auth } from "src/pages/FormRegister/Auth";
import HomePage from "src/pages/homepage/HomePage";
import UserInfo from "src/pages/UserInfo/UserInfo";
import AddGoods from "src/pages/AddGoods/AddGoods";
import { OAuthSuccess } from "src/pages/OAuthSuccess";
import { Login } from "src/pages/FormRegister/sign-in";
import { SignUp } from "src/pages/FormRegister/sign-up";
import { SearchResults } from "src/pages/SearchResults";
import { ProductPage } from "src/pages/ProductPage/ProductPage";
import FourOhFourPage from "src/pages/ErrorPages/FourOhFourPage";

import { route } from "./routeConstants";
import { AuthorizedRoute, UnauthorizedRoute } from "./protectedRoute";

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
