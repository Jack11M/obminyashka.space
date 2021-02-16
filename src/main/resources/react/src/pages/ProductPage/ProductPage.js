import React , { useEffect , useState } from 'react';

import ProductPhotoCarousel from './ProductPhotoCarousel/ProductPhotoCarousel';
import ProductDescription from './ProductDescription/ProductDescription';
import ProductOwnerData from './ProductOwnerData/ProductOwnerData';
import ProductPostData from './ProductPostData/ProductPostData';
import ProductOffers from './ProductOffers/ProductOffers';
import TitleBigBlue from '../../components/title_Big_Blue';
import { getProduct } from '../../REST/Resources/fetchProfile';

import './ProductPage.scss';

	const ProductPage = () => {
	
	const [ product , setProduct ] = useState( {} );
	const [ photos , setPhotos ] = useState( [] );
	const [ wishes , setWishes ] = useState( [] );
	
	useEffect( () => {
		getProduct( 3 )
			.then( ( { data } ) => {
				const { images , wishesToExchange , ...rest } = data;
				const arrWishes = wishesToExchange.split( ', ' );
				if ( rest.readyForOffers ) {
					arrWishes.push( 'ваши предложения' );
				}
				setWishes( arrWishes );
				setPhotos( images );
				setProduct( rest );
			} )
			.catch( e => { console.log( e ); } );
	} , [] );
	
	return (
		<div>
			<section className = 'topSection'>
				<div className = 'productPageContainer'>
					<div className = 'breadСrumbs'>Категории /
						Категория/
						{ product.subcategoryId } /
						<span>{ product.topic }</span>
					</div>
					<div className = 'productPageInner'>
						<div className = 'carouselAndDescription'>
							<ProductPhotoCarousel photos = { photos }/>
							<ProductDescription
								title = { product.topic }
								description = { product.description }
							/>
						</div>
						<div className = 'ownerAndPost'>
							<ProductOwnerData
							
							/>
							<ProductPostData
								title = { product.topic }
								wishes = { wishes }
								size = { product.size }
								gender = { product.gender }
								age = { product.age }
								season = { product.season }
							/>
						</div>
					</div>
				</div>
			</section>
			<section>
				<div className = 'productPageContainer'>
					<div className = 'productPageInner'>
						<div className = 'sectionHeading'>
							<TitleBigBlue text = { 'Вас так же могут заинтересовать' }/>
						</div>
						<ProductOffers/>
					</div>
				</div>
			</section>
		</div>
	);
};

export default ProductPage;