import React from "react";
import { Switch, Route } from "react-router-dom";
import Form from "./FormRegister/Form";

export default () => {
  return (
    <Switch>
      <Route path="/registration" exact component={Form} />
      <Route path="/registration/register" component={Form} />
    </Switch>
  );
};
