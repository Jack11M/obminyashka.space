import React from "react";
import { Route, Switch } from "react-router-dom";

import LogIn from './LogIn';
import SignUp from './SignUp';

export default () => {
  return (
    <Switch>
      <Route path="/logIn/" exact component={LogIn} />
      <Route path="/logIn/signUp" component={SignUp} />
    </Switch>
  );
};
