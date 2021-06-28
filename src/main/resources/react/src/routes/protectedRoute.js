import React from 'react';
import { Redirect, Route } from 'react-router-dom';

const Protected = ({ path, component, permission, redirect }) => {
  return permission ? (
    <Route path={path} component={component} />
  ) : (
    <Redirect to={redirect} />
  );
};
export default Protected;
