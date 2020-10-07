import React from 'react';
import styled from 'styled-components';

const ProfileInput = styled.div`
 display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 21px;
`

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
  font-size: 16px;
  line-height: 16px;
  :focus,
  :hover {
    border: 1px solid #464444;
  };
  font-family: inherit;
`;

const InputProfile = (props) => {
	const {name, label, value, type} = props.data;

	return (
		<ProfileInput >
			<Label htmlFor={name}>{`${label}`}</Label>
			<Input name={name} value={value} onChange={props.click} type={type}/>
		</ProfileInput>
	);
};

export default InputProfile;
