import React from "react";
import "./title.scss";

const Title_Big_Blue = ({whatClass, text}) => {
  return (
    <>
      <h2 className={`titleBigBlue ${whatClass}`}>{text}</h2>
    </>
  );
};

export default Title_Big_Blue;
