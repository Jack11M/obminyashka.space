import React from 'react';
import { useSelector } from 'react-redux';

import Protected from './protectedRoute';
import Public from './publicRoute';

export default () => {
	const { isAuthenticated } = useSelector( state => state.auth );
	return isAuthenticated ? (<Protected/>) : (<Public/>);
};
