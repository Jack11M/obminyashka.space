import React from "react";
import { Route, Switch } from "react-router-dom";

import Login from './Login';
import SignUp from './SignUp';

export default () => {
  return (
    <Switch>
      <Route path="/login/" exact component={Login} />
      <Route path="/login/signup" component={SignUp} />
    </Switch>
  );
};
