import React from 'react';
import { useSelector } from 'react-redux';

import './spinner.scss';

const Spinner = () => {
	const {isFetching} = useSelector(state => state.ui);
	return isFetching ? (<div className="loader">
		<div className="inner one"/>
		<div className="inner two"/>
		<div className="inner three"/>
	</div>) : null;
};

export default Spinner;
