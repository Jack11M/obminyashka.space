import React, { useEffect, useState } from 'react';
import { useRouteMatch } from 'react-router-dom';
import { useSelector } from 'react-redux';

import ProductPhotoCarousel from './ProductPhotoCarousel/ProductPhotoCarousel';
import ProductDescription from './ProductDescription/ProductDescription';
import { getTranslatedText } from '../../components/local/localisation';
import ProductOwnerData from './ProductOwnerData/ProductOwnerData';
import ProductPostData from './ProductPostData/ProductPostData';
import TitleBigBlue from '../../components/common/title_Big_Blue';
import ProductOffers from './ProductOffers/ProductOffers';
import { getProduct } from '../../REST/Resources';

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

  const ageConversion = (age) => {
    let resultAge;
    switch (age) {
      case 'YOUNGER_THAN_1':
        resultAge = '0-1';
        break;
      case 'FROM_1_TO_2':
        resultAge = '1-2';
        break;
      case 'FROM_3_TO_5':
        resultAge = '3-5';
        break;
      case 'FROM_6_TO_9':
        resultAge = '6-9';
        break;
      case 'FROM_10_TO_12':
        resultAge = '10-12';
        break;
      case 'FROM_12_TO_14':
        resultAge = '12-14';
        break;
      case 'OLDER_THAN_14':
        resultAge = '14+';
        break;
    }
    return resultAge;
  };

  const genderConversion = (gender, lang) => {
    let resultGender;
    switch (gender) {
      case 'MALE':
        resultGender = getTranslatedText('productEnums.gender.male', lang);
        break;
      case 'FEMALE':
        resultGender = getTranslatedText('productEnums.gender.female', lang);
        break;
      case 'UNSELECTED':
        resultGender = getTranslatedText(
          'productEnums.gender.unselected',
          lang
        );
        break;
    }
    return resultGender;
  };

  const seasonConversion = (season) => {
    let resultSeason;
    switch (season) {
      case 'SUMMER':
        resultSeason = getTranslatedText('productEnums.season.summer', lang);
        break;
      case 'WINTER':
        resultSeason = getTranslatedText('productEnums.season.winter', lang);
        break;
      case 'ALL_SEASONS':
        resultSeason = getTranslatedText(
          'productEnums.season.allSeasons',
          lang
        );
        break;
      case 'DEMI_SEASON':
        resultSeason = getTranslatedText(
          'productEnums.season.demiSeason',
          lang
        );
        break;
    }
    return resultSeason;
  };

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
                gender={genderConversion(product.gender)}
                age={ageConversion(product.age)}
                season={seasonConversion(product.season)}
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
