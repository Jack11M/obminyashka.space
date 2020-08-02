import React from "react";
import "./inputData.scss";

const InputData = (props) => {
  const { fieldId, label, value } = props.data;

  return (
    <div className={"input-data"}>
      <label htmlFor={fieldId}>{`${label}:`}</label>
      <input id={fieldId} value={value} onChange={props.addInput}/>
    </div>
  );
};

export default InputData;
