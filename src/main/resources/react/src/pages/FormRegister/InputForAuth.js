import React from 'react';
import {  useLocation } from 'react-router-dom';
import { InputAuth, InputDiv, Label } from './loginStyle';

const InputForAuth = ( { text, name, type, error, click } ) => {
	const location = useLocation();
	const path = location.pathname === '/logIn/';

	return (
		<InputDiv path={path}>
			<Label>{ text }
				<InputAuth name={ name } type={ type } onChange={click}/>
			</Label>
			<span>{ error }</span>
		</InputDiv>
	);
};

export default InputForAuth;