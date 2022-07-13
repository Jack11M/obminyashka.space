import { useCallback, useEffect, useState } from 'react';
import { useSelector } from 'react-redux';
import { useLocation, useParams } from 'react-router-dom';

import api from 'REST/Resources';
import { enumAge } from 'config/ENUM';
import { getLang, getProfile } from 'store/auth/slice';
import TitleBigBlue from 'components/common/title_Big_Blue';
import { getTranslatedText } from 'components/local/localization';

import { getDate } from './helpers';
import ProductOffers from './ProductOffers';
import ProductPostData from './ProductPostData';
import ProductOwnerData from './ProductOwnerData';
import ProductDescription from './ProductDescription';
import ProductPhotoCarousel from './ProductPhotoCarousel';

import {
  TopSection,
  BreadCrumbs,
  OwnerAndPost,
  BottomSection,
  SectionHeading,
  ProductPageInner,
  ProductPageContainer,
  CarouselAndDescription,
} from './styles';

const ProductPage = () => {
  const { id } = useParams();
  const location = useLocation();
  const lang = useSelector(getLang);
  const profile = useSelector(getProfile);

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
          console.log(e);
        });
    }
  }, [lang, id, location, setPreviewData]);

  return (
    <>
      <TopSection>
        <ProductPageContainer>
          <BreadCrumbs>
            {getTranslatedText('product.categories', lang)}/
            {getTranslatedText(`categories.${category.name}`, lang)}/
            {getTranslatedText(`categories.${subcategory.name}`, lang)}/
            {product.topic}
          </BreadCrumbs>

          <ProductPageInner>
            <CarouselAndDescription>
              <ProductPhotoCarousel photos={photos} />
              <ProductDescription
                title={product.topic}
                description={product.description}
              />
            </CarouselAndDescription>

            <OwnerAndPost>
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
            </OwnerAndPost>
          </ProductPageInner>
        </ProductPageContainer>
      </TopSection>

      <BottomSection>
        <ProductPageContainer>
          <ProductPageInner>
            <SectionHeading>
              <TitleBigBlue
                text={getTranslatedText('product.blueTitle', lang)}
              />
            </SectionHeading>

            <ProductOffers />
          </ProductPageInner>
        </ProductPageContainer>
      </BottomSection>
    </>
  );
};
export default ProductPage;
