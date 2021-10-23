import React, { useEffect, useState } from 'react';
import { useRouteMatch } from 'react-router-dom';
import { useSelector } from 'react-redux';

import ProductPhotoCarousel from './ProductPhotoCarousel/ProductPhotoCarousel';
import ProductDescription from './ProductDescription/ProductDescription';
import { getTranslatedText } from 'components/local/localisation';
import ProductOwnerData from './ProductOwnerData/ProductOwnerData';
import ProductPostData from './ProductPostData/ProductPostData';
import TitleBigBlue from 'components/common/title_Big_Blue';
import ProductOffers from './ProductOffers/ProductOffers';
import { getProduct } from 'REST/Resources';

import './ProductPage.scss';

const ProductPage = () => {
  const param = useRouteMatch();
  const { lang } = useSelector((state) => state.auth);
  const [product, setProduct] = useState({});
  const [photos, setPhotos] = useState([]);
  const [wishes, setWishes] = useState([]);
  const [location, setLocation] = useState({});
  const [category, setCategory] = useState({});
  const [subcategory, setSubcategory] = useState({});

  useEffect(() => {
    getProduct(param.params.id)
      .then(({ data }) => {
        const {
          images,
          wishesToExchange,
          category,
          subcategory,
          location,
          ...rest
        } = data;
        const arrWishes = wishesToExchange.split(', ');
        if (rest.readyForOffers) {
          arrWishes.push(getTranslatedText('product.checkInUl', lang));
        }
        setWishes(arrWishes);
        setPhotos(images);
        setProduct(rest);
        setCategory(category);
        setSubcategory(subcategory);
        setLocation(location);
      })
      .catch((e) => {
        console.log(e);
      });
  }, [lang]);

  return (
    <div>
      <section className="topSection">
        <div className="productPageContainer">
          <div className="breadСrumbs">
            {getTranslatedText('product.categories', lang)}/{category.name}/
            {subcategory.name}/{product.topic}
          </div>
          <div className="productPageInner">
            <div className="carouselAndDescription">
              <ProductPhotoCarousel photos={photos} />
              <ProductDescription
                title={product.topic}
                description={product.description}
              />
            </div>
            <div className="ownerAndPost">
              <ProductOwnerData
                ava={product.ownerAvatar}
                name={product.ownerName}
                date={product.createdDate}
                city={location.city}
                phone={product.phone}
              />
              <ProductPostData
                title={product.topic}
                wishes={wishes}
                size={product.size}
                gender={getTranslatedText(`genderEnum.${product.gender}`, lang)}
                age={product.age}
                season={ getTranslatedText(`seasonEnum.${product.season}`, lang) }
              />
            </div>
          </div>
        </div>
      </section>
      <section>
        <div className="productPageContainer">
          <div className="productPageInner">
            <div className="sectionHeading">
              <TitleBigBlue
                text={getTranslatedText('product.blueTitle', lang)}
              />
            </div>
            <ProductOffers />
          </div>
        </div>
      </section>
    </div>
  );
};
export default ProductPage;
