import React from "react";
import { Route, Switch } from "react-router-dom";
import HomePage from "../pages/homepage/HomePage.js";
import Auth from "../pages/FormRegister/Auth.js";
import UserInfo from "../pages/UserInfo/UserInfo";

export default () => {
  return (
    <Switch>
      <Route path="/" component={HomePage} exact />
      <Route path="/userInfo/" component={UserInfo} />
      <Route path="/registration/" component={Auth} />
      <Route path="/registration/register" exact component={Auth} />
    </Switch>
  );
};
