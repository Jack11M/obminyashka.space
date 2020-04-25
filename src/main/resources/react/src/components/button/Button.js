import React, { Fragment } from "react";
import "./button.scss";

const Button = ({ whatClass, disabling, text, click = null }) => {
  return (
    <Fragment>
      <button
        className={whatClass}
        disabled={disabling || null}
        onClick={click}
      >
        {text}
      </button>
    </Fragment>
  );
};
export default Button;
