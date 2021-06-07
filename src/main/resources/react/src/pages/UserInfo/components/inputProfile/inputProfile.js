import React from 'react';
import styled from 'styled-components';

import { getCurrentDate, getMinDate } from '../../../../Utils';

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

const InputProfile = ( { id = '', type, name, label, value, change, errors=[] } ) => {
	// I will delete it later, I need it
	// const { errors, errorsPhone, errorsChildren } = useSelector( state => state.profileMe );
	// let error;
	// if (type === 'phone') {
	// 	error = errorsPhone.find( error => error.key === id );
	// } else if (type === 'date') {
	// 	error = errorsChildren.find( error => error.key === id );
	// } else {
	// 	error = errors.find( error => error.key === name );
	// }
	// const errorText = error ? error.errorText : null;
	const error = !!errors.length && errors.find(err => {
		return Object.keys(err).join('')=== name});

	return (
		<ProfileInput>
			<Label htmlFor={ name + id }>{ `${ label }` }</Label>
			<Input
				max={ type === 'date' ? getCurrentDate() : null }
				min={ type === 'date' ? getMinDate() : null }
				id={ name + id }
				type={ type }
				name={ name }
				value={ value }
				error={ error }
				onChange={ change }
				placeholder={ name === 'phoneNumber' ? `+38(123)456-78-90, 381234567890` : null }
			/>
			<SpanError>{ error && error[name] }</SpanError>
		</ProfileInput>
	);
};

export default InputProfile;
