import React , { Component } from 'react';
import './ProductPhotoCarousel.scss';
import Slider from 'react-slick';

import { lot1, lot2, lot3, lot4, lot5, lot6, lot7 } from '../../../img/all_images_export/cards'

export default class ProductPhotoCarousel extends Component {

	render() {
		const settings = {
			dots: false,
			infinite: true,
			slidesToShow: 4,
			slidesToScroll: 1,
			centerMode: false,
			vertical: true,
			verticalSwiping: true,
		};
		return (
			<div className = 'carouselBox'>
				<div className = 'sliderPosition' >
					<Slider { ...settings } className = 'productPhotoSlider'  >
						<div>
							<img src = {lot1} alt = '1'/>
						</div>
						<div>
							<img src = {lot2} alt = '2'/>
						</div>
						<div>
							<img src = {lot3} alt = '3'/>
						</div>
						<div>
							<img src = {lot4} alt = '4'/>
						</div>
						<div>
							<img src = {lot5} alt = '5'/>
						</div>
						<div>
							<img src = {lot6} alt = '6'/>
						</div>
					</Slider>
				</div>
				<div className = 'productPhotoSlideBig'>
					<img src = {lot7} alt = '6'/>
				</div>
			</div>
		);
	}
}
