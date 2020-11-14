import React from "react";
import "./title.scss";

const TitleBigBlue = ({whatClass = '', text}) => {
  return (
    <>
      <h2 className={`titleBigBlue ${whatClass}`}>{text}</h2>
    </>
  );
};

export default TitleBigBlue;
