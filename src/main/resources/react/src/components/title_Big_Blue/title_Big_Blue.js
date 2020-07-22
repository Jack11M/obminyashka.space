import React from "react";
import "./title.scss";

const Title_Big_Blue = (props) => {
  return (
    <>
      <h2 className={`titleBigBlue ${props.whatClass}`}>{props.text}</h2>
    </>
  );
};

export default Title_Big_Blue;
