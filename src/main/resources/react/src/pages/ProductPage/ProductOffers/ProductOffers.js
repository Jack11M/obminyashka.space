import React from 'react';

import ProductCard from '../../../components/item-card';

import './ProductOffers.scss';

const ProductOffers = () => {
	return (
		<div className = 'offersBoxes'>
			<ProductCard
				margin={'0'}
				city = { 'Харьков' }
				text = { `Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет` }
				picture = { 'https://static.toiimg.com/photo/72975551.cms' }
			/>
			<ProductCard
				margin={'0'}
				city = { 'Харьков' }
				text = { `Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет` }
				picture = { 'https://static.toiimg.com/photo/72975551.cms' }
			/>
			<ProductCard
				margin={'0'}
				city = { 'Харьков' }
				text = { `Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет` }
				picture = { 'https://static.toiimg.com/photo/72975551.cms' }
			/>
			<ProductCard
				margin={'0'}
				city = { 'Харьков' }
				text = { `Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет` }
				picture = { 'https://static.toiimg.com/photo/72975551.cms' }
			/>
		</div>
	);
};
export default ProductOffers;
