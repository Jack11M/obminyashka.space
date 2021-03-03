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
	const [ location , setLocation ] = useState( {} );
	const [ category , setCategory ] = useState( {} );
	const [ subcategory , setSubcategory ] = useState( {} );
	// const [ ownerAvatar , setOwnerAvatar ] = useState( );
	
	useEffect( () => {
		getProduct( 4 )
			.then( ( { data } ) => {
				const { images , wishesToExchange , category , subcategory , location , ...rest } = data;
				const arrWishes = wishesToExchange.split( ', ' );
				if ( rest.readyForOffers ) {
					arrWishes.push( 'your suggestions' );
				}
				setWishes( arrWishes );
				setPhotos( images );
				setProduct( rest );
				setCategory( category );
				setSubcategory( subcategory );
				setLocation( location );
				// setOwnerAvatar( ownerAvatar );
				
			} )
			.catch( e => { console.log( e ); } );
	} , [] );
	
	return (
		<div>
			<section className = 'topSection'>
				<div className = 'productPageContainer'>
					<div className = 'breadСrumbs'>Categories/{ category.name }/{ subcategory.name }/{ product.topic }
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
								ava = { product.ownerAvatar }
								name = { product.ownerName }
								date = { product.createdDate }
								city = { location.city }
								phone = { product.phone }
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
