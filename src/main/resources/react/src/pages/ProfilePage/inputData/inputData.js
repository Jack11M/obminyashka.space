import React from "react";
import "./inputData.scss";

const InputData = ({ data }) => {
  const { fieldId, label, value } = data;

  return (
    <div className={"input-data"}>
      <label htmlFor={fieldId}>{`${label}:`}</label>
      <input id={fieldId} value={value} />
    </div>
  );
};

export default InputData;
