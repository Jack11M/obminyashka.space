import React , { useState } from 'react';
import Slider from 'react-slick';

import ProductDB from '../MokDB';
import './ProductPhotoCarousel.scss';

const ProductPhotoCarousel = ({photos}) => {
	
	const [ state , setState ] = useState( {
		photos : ProductDB.photo ,
		bigPhoto : ProductDB.photo[0]
	} );
	
	const settings = {
		dots : false ,
		infinite : true ,
		slidesToShow : 4 ,
		slidesToScroll : 1 ,
		centerMode : false ,
		vertical : true ,
		verticalSwiping : true ,
	};
	
	const showBigImg = ( id ) => {
		setState( { ...state , bigPhoto : ProductDB.photo[id] } );
	};
	
	return (
		<div className = 'carouselBox'>
			<div className = 'sliderPosition'>
				<Slider { ...settings } className = 'productPhotoSlider'>
					{ state.photos.map( ( photo , idx ) =>
						<div key = { `index-${ idx }` }>
							<img
								src = { photo }
								alt = { idx }
								onClick = { () => showBigImg( idx ) }
								className = { state.bigPhoto === photo ? 'selected' : '' }
							/>
						</div>
					) }
				</Slider>
			</div>
			<div className = 'productPhotoSlideBig'>
				<img src = { state.bigPhoto } alt = 'activeSlide'/>
			</div>
		</div>
	);
};

export default ProductPhotoCarousel;