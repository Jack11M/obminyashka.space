import React from "react";
import styled from "styled-components";

import "./inputProfile.scss";

const Label = styled.label`
  font-size: 14px;
  line-height: 16px;
  display: inline-flex;
`;

const Input = styled.input`
  display: inline-flex;
  box-sizing: border-box;
  width: 415px;
  padding: 12px 16px 11px 16px;
  border: 1px solid #bcbcbc;
  border-radius: 2px;
  outline: none;
  :focus,
  :hover {
    border: 1px solid #464444;
  }
`;

const InputProfile = (props) => {
  const { name, label, value } = props.data;

  return (
    <div className={"input-Profile"}>
      <Label htmlFor={name}>{`${label}:`}</Label>
      <Input name={name} value={value}   onChange={props.click} />
    </div>
  );
};

export default InputProfile;
