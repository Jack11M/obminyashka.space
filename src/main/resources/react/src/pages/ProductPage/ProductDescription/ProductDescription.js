import React from 'react';
import './ProductDescription.scss';
import ProductDB from '../MokDB';

const ProductDescription = () => {
	return (
		<div className = 'descriptionBox'>
			<div className = 'descriptionHeading'><h2>{ ProductDB.description[0] }</h2></div>
			<div className = 'descriptionText'><p>{ ProductDB.description[1] }</p></div>
		</div>
	);
};
export default ProductDescription;