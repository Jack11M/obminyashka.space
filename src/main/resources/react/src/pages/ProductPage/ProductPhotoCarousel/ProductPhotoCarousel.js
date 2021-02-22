import React , { useState } from 'react';

import { ProductDB } from '../MokDB';
import MappingArr from '../mappingArr';

import './ProductPhotoCarousel.scss';
import NoPhoto from './NoPhoto';

const ProductPhotoCarousel = () => {
	
	const [ state , setState ] = useState( {
		photos : ProductDB.photo ,
		bigPhoto : ProductDB.photo[0]
	} );
	
	const showBigImg = ( id ) => {
		setState( { ...state , bigPhoto : ProductDB.photo[id] } );
	};
	let noArr;
	if ( !state.photos.length ) {
		noArr = <NoPhoto noPhoto='bigNoPhoto'/>;
	}
	else {
		noArr = <img src = { state.bigPhoto } alt = 'activeSlide'/>;
	}
	
	return (
		<div className = 'carouselBox'>
			<div className = 'sliderPosition'>
				<MappingArr
					photos = { state.photos }
					showBigImg = { showBigImg }
					bigPhoto = { state.bigPhoto }
				/>
			</div>
			<div className = 'productPhotoSlideBig'>
				{ noArr }
			</div>
		</div>
	);
};
export default ProductPhotoCarousel;
