import React from "react";

const InputForForm = props => {
  const { id, text, type, dataError, errorStatus } = props.dataInput;

  return (
    <div className="input_field">
      <label htmlFor={id}>{text}</label>
      <input
        id={id}
        name={id}
        type={type}
        onChange={e => props.isValid(e.target)}
      />
      <span data-error={dataError}>{errorStatus}</span>
    </div>
  );
};
export default InputForForm;
