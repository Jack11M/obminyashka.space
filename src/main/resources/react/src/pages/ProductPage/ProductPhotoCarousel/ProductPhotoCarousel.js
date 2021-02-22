import React , { useState } from 'react';

import MappingArr from '../mappingArr';
import NoPhoto from './NoPhoto';

import './ProductPhotoCarousel.scss';

const ProductPhotoCarousel = ( photos ) => {
	
	const [ state , setState ] = useState( {
		photos : photos ,
		bigPhoto : photos[0]
	} );
	
	const showBigImg = ( id ) => {
		setState( { ...state , bigPhoto : photos[id] } );
	};
	
	let noArr;
	if ( !state.photos.length ) {
		noArr = <NoPhoto noPhoto = 'bigNoPhoto' noPhotoImg = 'bigNoPhotoImg'/>;
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
