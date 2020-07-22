import React from "react";
import "./inputChildren.scss";
import InputData from "../inputData";

const InputChildren = (props) => {
  const { fields, addField, deleteField } = props.data;
  console.log(fields);

  return (
    <>
      {fields.map((item) => {
        return <InputData data={item} />;
      })}
    </>
  );
};

export default InputChildren;
