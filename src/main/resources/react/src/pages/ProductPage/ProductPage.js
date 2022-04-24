import { enumAge } from 'config/ENUM';
import React, { useEffect, useState } from 'react';
import { useMatch, useLocation } from 'react-router-dom';
import { useSelector } from 'react-redux';

import api from 'REST/Resources';
import { getDate } from './helpers';
import ProductOffers from './ProductOffers/ProductOffers';
import TitleBigBlue from 'components/common/title_Big_Blue';
import ProductPostData from './ProductPostData/ProductPostData';
import { getTranslatedText } from 'components/local/localization';
import ProductOwnerData from './ProductOwnerData/ProductOwnerData';
import ProductDescription from './ProductDescription/ProductDescription';
import ProductPhotoCarousel from './ProductPhotoCarousel/ProductPhotoCarousel';

import './ProductPage.scss';

const ProductPage = () => {
  const { lang, profile } = useSelector((state) => state.auth);

  const param = useMatch();
  const location = useLocation();
  const { id } = param.params;

  console.log(param);

  const [photos, setPhotos] = useState([]);
  const [wishes, setWishes] = useState([]);
  const [product, setProduct] = useState({});
  const [category, setCategory] = useState({});
  const [subcategory, setSubcategory] = useState({});
  const [currentLocation, setCurrentLocation] = useState({});

  const setPreviewData = () => {
    const { state } = location;
    setCategory(state.category);
    setSubcategory(state.subcategory);
    setCurrentLocation(state.currentLocation);
    setWishes(state.wishes);
    setProduct(state.product);
    setPhotos(state.photos);
  };

  useEffect(() => {
    if (location.state) setPreviewData();
    else {
      api.fetchProduct
        .getProduct(id)
        .then(({ data }) => {
          const {
            images,
            wishesToExchange,
            category,
            subcategory,
            location,
            ...rest
          } = data;
          setWishes(wishesToExchange.split(', '));
          setPhotos(images);
          setProduct(rest);
          setCategory(category);
          setSubcategory(subcategory);
          setCurrentLocation(location);
        })
        .catch((e) => {
          console.log(e);
        });
    }
  }, [lang, id, location]);

  return (
    <div>
      <section className="topSection">
        <div className="productPageContainer">
          <div className="breadСrumbs">
            {getTranslatedText('product.categories', lang)}/
            {getTranslatedText(`categories.${category.name}`, lang)}/
            {getTranslatedText(`categories.${subcategory.name}`, lang)}/
            {product.topic}
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
                phone={product.phone}
                city={currentLocation.city}
                date={product.createdDate || getDate(lang)}
                name={product.ownerName || profile.username}
                ava={product.ownerAvatar || profile.avatarImage}
              />

              <ProductPostData
                wishes={wishes}
                size={product.size}
                title={product.topic}
                readyForOffers={product.readyForOffers}
                age={enumAge[product.age] || product.age}
                gender={getTranslatedText(`genderEnum.${product.gender}`, lang)}
                season={getTranslatedText(`seasonEnum.${product.season}`, lang)}
              />
            </div>
          </div>
        </div>
      </section>

      <section className="bottomSection">
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
