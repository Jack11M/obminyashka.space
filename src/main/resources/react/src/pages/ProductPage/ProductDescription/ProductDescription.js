import React from 'react';
import './ProductDescription.scss';
import ProductDB from '../MokDB';

const ProductDescription = ( { title , description } ) => {
	return (
		<div className = 'descriptionBox'>
			<div className = 'descriptionHeading'><h2>{ title }</h2></div>
			<div className = 'descriptionText'><p>{ description }</p></div>
		</div>
	);
};
export default ProductDescription;