import React from "react";
import "./inputChildrenList.scss";
import InputData from "../inputData/inputData";

const InputChildrenList = ({ data }) => {
  return (
    <>
      {data.map((item) => (
        <InputData data={item} key={item.fieldId} />
      ))}
    </>
  );
};

export default InputChildrenList;
