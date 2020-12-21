import React from "react";
import { Route, Switch, Redirect } from "react-router-dom";
import { useSelector } from 'react-redux';

import HomePage from "../pages/homepage/HomePage.js";
import Auth from "../pages/FormRegister/Auth.js";
import UserInfo from "../pages/UserInfo/UserInfo";
import FourOhFourPage from '../pages/ErrorPages/FourOhFourPage';

export default () => {
  const {isAuthenticated} = useSelector(state => state.auth);

  return isAuthenticated ? (
    <Switch>
      <Route path="/" component={HomePage} exact />
      <Route path="/userInfo/" component={UserInfo} />
      <Route  component={FourOhFourPage}/>
    </Switch>
  ) : (
    <Switch>
      <Route path="/" component={HomePage} exact />
      <Route path="/logIn/" component={Auth} />
      {/*<Route path="/logIn/signUp" component={Auth} />*/}
      <Redirect to={"/logIn/"}/>
    </Switch>
  );
};
