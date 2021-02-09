import React from "react";
import { Redirect, Route, Switch } from 'react-router-dom';

import Login from './Login';
import SignUp from './SignUp';

export default () => {
  return (
    <Switch>
      <Route path="/login" exact component={Login} />
      <Route path="/login/signup" exact component={SignUp} />
      <Redirect to={"/login"}/>
    </Switch>
  );
};
