import React from 'react';
import Slider from 'react-slick';
import NoPhoto from './ProductPhotoCarousel/NoPhoto';

const settings = {
	dots : false ,
	infinite : true ,
	slidesToShow : 4 ,
	slidesToScroll : 1 ,
	centerMode : false ,
	vertical : true ,
	verticalSwiping : false ,
};

const MappingArr = ( { photos , showBigImg , bigPhoto } ) => {
	
	let variable;
	
	if ( !photos.length ) {
		variable = <NoPhoto/>;
	}
	else
		if ( photos.length < 5 ) {
			variable = <>
				{ photos.map( ( photo , idx ) =>
					<div key = { `index-${ idx }` }>
						<img
							src = { photo }
							alt = { idx }
							onClick = { () => showBigImg( idx ) }
							className = { `noCarouselImg ${ bigPhoto === photo ? 'selected' : '' }` }
						/>
					</div>
				) }
			</>;
		}
		else {
			variable = <Slider { ...settings } className = 'productPhotoSlider'>
				{ photos.map( ( photo , idx ) =>
					<div key = { `index-${ idx }` }>
						<img
							src = { photo }
							alt = { idx }
							onClick = { () => showBigImg( idx ) }
							className = { bigPhoto === photo ? 'selected' : '' }
						/>
					</div>
				) }
			</Slider>;
		}
	return (
		variable
	);
};
export default MappingArr;
