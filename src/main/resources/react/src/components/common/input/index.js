import React, { memo } from 'react';
import { useLocation } from 'react-router-dom';

import { route } from '../../../routes/routeConstants';

import { InputAuth, InputDiv, Label, SpanError } from './styles';

const Index = ( { text, name, type, value, errors, click } ) => {
	const location = useLocation();
	const path = location.pathname === route.login;
	const error = !!errors.length && errors.find(err => Object.keys(err).join('')=== name);

	return (
		<InputDiv path={ path }>
			<Label>{ text }
				<InputAuth name={ name } type={ type } value={ value } error={ error && error[name] } onChange={ click }/>
			</Label>
			<SpanError>{error && error[name]}</SpanError>
		</InputDiv>
	);
};

export default memo( Index );
