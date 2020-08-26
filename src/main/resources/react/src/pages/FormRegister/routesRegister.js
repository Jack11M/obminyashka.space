import React from "react";
import { Route, Switch } from "react-router-dom";
import Form from "./Form.js";

export default () => {
  return (
    <Switch>
      <Route path="/registration/" exact component={Form} />
      <Route path="/registration/register" component={Form} />
    </Switch>
  );
};
