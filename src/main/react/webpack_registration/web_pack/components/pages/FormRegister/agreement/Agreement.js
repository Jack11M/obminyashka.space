import React, { Fragment } from "react";
import SpanCheck from "./Span";

const Agreement = ({ whatIsPage }) => {
  const loginText = `Запомнить меня`;
  const textForLabel = whatIsPage ? loginText : <SpanCheck />;
  return <Fragment>{textForLabel}</Fragment>;
};
export default Agreement;
