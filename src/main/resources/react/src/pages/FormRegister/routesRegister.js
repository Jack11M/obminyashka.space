import React from "react";
import { Route, Switch } from "react-router-dom";

import LogIn from './LogIn';
import SignUp from './SignUp';
import Form from './Form';


export default () => {
  return (
    <Switch>
      {/*<Route path="/logIn/signUp" exact component={Form} />*/}
      {/*<Route path="/logIn/" component={Form} />*/}
      <Route path="/logIn/" exact component={LogIn} />
      <Route path="/logIn/signUp" component={SignUp} />
    </Switch>
  );
};
