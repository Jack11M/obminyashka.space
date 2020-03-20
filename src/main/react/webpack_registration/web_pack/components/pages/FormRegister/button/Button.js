import React, { Fragment } from "react";

export const Button = ({ whatIsForm, disabling }) => {
  return (
    <Fragment>
      <button className="btn-auth" disabled={disabling}>
        {(whatIsForm && "Войти") || "Регистрация"}
      </button>
    </Fragment>
  );
};
