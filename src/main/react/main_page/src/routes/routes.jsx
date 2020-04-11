import { Switch, Route } from "react-router-dom";
import React from "react";
import HomePage from "../components/pages/homepage/HomePage.jsx";
import Auth from "../components/pages/FormRegister/Auth.jsx";

export default () => {
  return (
    <Switch>
      <Route path="/" component={HomePage} exact />
      <Route path="/registration/" component={Auth} />
      <Route path="/registration/register" exact component={Auth} />
    </Switch>
  );
};
