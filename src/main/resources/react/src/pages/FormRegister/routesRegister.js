import React from "react";
import { Redirect, Route, Switch } from 'react-router-dom';

import Login from './Login';
import SignUp from './SignUp';
import { route } from '../../routes/routeConstants';

export default () => {
  return (
    <Switch>
      <Route path={route.login} exact component={Login} />
      <Route path={`${route.login}${route.signUp}`} exact component={SignUp} />
      <Redirect to={route.login}/>
    </Switch>
  );
};
