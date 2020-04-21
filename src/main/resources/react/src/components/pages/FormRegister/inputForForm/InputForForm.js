import React from "react";

const InputForForm = props => {
  const { id, text, type, dataError, errorStatus } = props.dataInput;

  return (
    <div className="input_field">
      <label htmlFor={id}>{text}</label>
      <input id={id} name={id} type={type} onChange={props.isValid} />
      <span data-error={dataError}>{errorStatus}</span>
    </div>
  );
};
export default InputForForm;
