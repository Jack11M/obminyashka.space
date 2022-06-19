import { useCallback, useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import { useSelector } from 'react-redux';
import { useLocation, useParams } from 'react-router-dom';

import api from 'REST/Resources';
import { enumAge } from 'config/ENUM';
import { getErrorMessage } from 'Utils/error';
import { BackButton, TitleBigBlue } from 'components/common';
import { getAuthLang, getAuthProfile } from 'store/auth/slice';
import { getTranslatedText } from 'components/local/localization';

import { getDate } from './helpers';
import ProductOffers from './ProductOffers';
import ProductPostData from './ProductPostData';
import ProductOwnerData from './ProductOwnerData';
import ProductDescription from './ProductDescription';
import ProductPhotoCarousel from './ProductPhotoCarousel';

import './ProductPage.scss';

const ProductPage = () => {
  const { id } = useParams();
  const location = useLocation();
  const lang = useSelector(getAuthLang);
  const profile = useSelector(getAuthProfile);

  const [photos, setPhotos] = useState([]);
  const [wishes, setWishes] = useState([]);
  const [product, setProduct] = useState({});
  const [category, setCategory] = useState({});
  const [subcategory, setSubcategory] = useState({});
  const [currentLocation, setCurrentLocation] = useState({});

  const setPreviewData = useCallback(() => {
    const { state } = location;
    setCategory(state.category);
    setSubcategory(state.subcategory);
    setCurrentLocation(state.currentLocation);
    setWishes(state.wishes);
    setProduct(state.product);
    setPhotos(state.photos);
  }, [location]);

  useEffect(() => {
    if (location.state) setPreviewData();
    else {
      api.fetchProduct
        .getProduct(id)
        .then(
          ({
            images,
            wishesToExchange,
            category: categoryValue,
            subcategory: subcategoryValue,
            location: locationValue,
            ...rest
          }) => {
            setWishes(wishesToExchange.split(', '));
            setPhotos(images);
            setProduct(rest);
            setCategory(categoryValue);
            setSubcategory(subcategoryValue);
            setCurrentLocation(locationValue);
          }
        )
        .catch((e) => {
          toast.error(getErrorMessage(e));
        });
    }
  }, [lang, id, location, setPreviewData]);

  return (
    <div>
      <section className="topSection">
        <div className="productPageContainer">
          <BackButton
            style={{ marginBottom: 16 }}
            text={getTranslatedText('button.back')}
          />

          <div className="breadСrumbs">
            {getTranslatedText('product.categories')}/
            {getTranslatedText(`categories.${category.name}`)}/
            {getTranslatedText(`categories.${subcategory.name}`)}/
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
                gender={getTranslatedText(`genderEnum.${product.gender}`)}
                season={getTranslatedText(`seasonEnum.${product.season}`)}
              />
            </div>
          </div>
        </div>
      </section>

      <section className="bottomSection">
        <div className="productPageContainer">
          <div className="productPageInner">
            <div className="sectionHeading">
              <TitleBigBlue text={getTranslatedText('product.blueTitle')} />
            </div>

            <ProductOffers />
          </div>
        </div>
      </section>
    </div>
  );
};
export default ProductPage;
