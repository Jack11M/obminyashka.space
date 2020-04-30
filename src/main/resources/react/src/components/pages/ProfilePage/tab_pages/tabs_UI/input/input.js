import React from "react";
import styled from "styled-components";

const Input = props => {
  const Div = styled.div`
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 21px;
  `;
  const Label = styled.label`
    font-size: 14px;
    line-height: 16px;
    display: inline-flex;
  `;
  const TabsInput = styled.input`
    display: inline-flex;
    box-sizing: border-box;
    width: 415px;
    padding: 12px 16px 11px 16px;
    border: 1px solid #e9e9e9;
    border-radius: 2px;
    outline: none;
    :focus,
    :hover {
      border: 1px solid #464444;
    }
  `;
  return (
    <Div>
      <Label>{props.text}</Label>
      <TabsInput />
    </Div>
  );
};

export default Input;
