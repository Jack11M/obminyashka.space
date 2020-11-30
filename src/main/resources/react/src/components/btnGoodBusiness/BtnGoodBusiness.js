import React from "react";
import { Link } from "react-router-dom";

const BtnGoodBusiness = props => {
  return (
    <Link to={props.href} className={props.whatClass}>
      <span className="icon-heart"/>
      {props.text}
    </Link>
  );
};

export default BtnGoodBusiness;
