import React from 'react';
import styled from 'styled-components';
import { useField } from 'formik';

const ProfileInput = styled.div`
  position: relative;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 21px;
`;

const Label = styled.label`
  font-size: 14px;
  line-height: 16px;
  display: inline-flex;
  cursor: pointer;
`;

const Input = styled.input`
  display: inline-flex;
  box-sizing: border-box;
  width: 415px;
  padding: 9px 16px 9px 16px;
  border: 1px solid #bcbcbc;
  border-radius: 2px;
  outline: none;
  font-size: 16px;
  line-height: 16px;
  font-family: inherit;
  border: 1px solid ${ ( { theme: { colors }, error } ) => error ? colors['colorError'] : 'hsl(0, 0%, 74%)' };
  color: ${ ( { theme: { colors } } ) => colors['right-color-text'] };

  &:focus, &:hover {
    border-color: ${ ( { theme: { colors }, error } ) => error ? colors['colorError'] : 'hsl(0, 0%, 44%)' };
  }
`;

const SpanError = styled.span`
  position: absolute;
  bottom: -17px;
  left: 135px;
  font-size: 11px;
  font-style: normal;
  font-weight: 400;
  line-height: 20px;
  color: ${ ( { theme: { colors } } ) => colors['colorError'] };
`;

const InputProfile = ( { id = '', label, ...props } ) => {

	const [ field, meta ] = useField( props );
	const { error, touched } = meta;
	return (
		<ProfileInput>
			<Label htmlFor={ id }>{ `${ label }` }</Label>
			<Input
				readOnly={props.readOnly}
				id={ field.name + id }
				error={ touched && error }
				{ ...field }
				{ ...props }
			/>
			{ <SpanError> { touched && error }</SpanError> }
		</ProfileInput>
	);
};


export default InputProfile;
