import React from 'react';
import { useLocation } from 'react-router-dom';
import { InputAuth, InputDiv, Label, SpanError } from './loginStyle';

const InputForAuth = ( { text, name, type, value, error, click } ) => {
	const location = useLocation();
	const path = location.pathname === '/login/';

	return (
		<InputDiv path={ path }>
			<Label>{ text }
				<InputAuth name={ name } type={ type } value={ value } error={ error } onChange={ click }/>
			</Label>
			<SpanError >{ error }</SpanError>
		</InputDiv>
	);
};

export default InputForAuth;
