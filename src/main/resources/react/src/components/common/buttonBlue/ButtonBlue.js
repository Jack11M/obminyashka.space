import React from "react";
import { Link } from "react-router-dom";

const ButtonBlue = props => {
  return (
    <Link to={props.href} className={props.whatClass}>
      {props.text}
    </Link>
  );
};

export default ButtonBlue;
