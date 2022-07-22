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
import { ProductPostData } from './ProductPostData';
import { ProductOwnerData } from './ProductOwnerData';
import ProductDescription from './ProductDescription';
import ProductPhotoCarousel from './ProductPhotoCarousel';

import {
  Span,
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
    setWishes(state.wishes);
    setPhotos(state.photos);
    setProduct(state.product);
    setCategory(state.category);
    setSubcategory(state.subcategory);
    setCurrentLocation(state.currentLocation);
  }, [location]);

  const getProduct = async () => {
    try {
      const {
        images,
        wishesToExchange,
        category: categoryValue,
        location: locationValue,
        subcategory: subcategoryValue,
        ...rest
      } = await api.fetchProduct.getProduct(id);

      setProduct(rest);
      setPhotos(images);
      setCategory(categoryValue);
      setSubcategory(subcategoryValue);
      setCurrentLocation(locationValue);
      setWishes(wishesToExchange?.split(', '));
    } catch (e) {
      toast.error(getErrorMessage(e));
    }
  };

  useEffect(() => {
    if (location.state) setPreviewData();
    else {
      getProduct();
    }
  }, [lang, id, location, setPreviewData]);

  return (
    <>
      <TopSection>
        <ProductPageContainer>
          <BackButton
            style={{ marginBottom: 16 }}
            text={getTranslatedText('button.back')}
          />

          <BreadCrumbs>
            {getTranslatedText('product.categories', lang)}/
            {getTranslatedText(`categories.${category?.name}`, lang)}/
            {getTranslatedText(`categories.${subcategory?.name}`, lang)}/
            <Span>{product.topic}</Span>
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
                city={currentLocation?.city}
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
            </OwnerAndPost>
          </ProductPageInner>
        </ProductPageContainer>
      </TopSection>

      <BottomSection>
        <ProductPageContainer>
          <ProductPageInner>
            <SectionHeading>
              <TitleBigBlue text={getTranslatedText('product.blueTitle')} />
            </SectionHeading>

            <ProductOffers />
          </ProductPageInner>
        </ProductPageContainer>
      </BottomSection>
    </>
  );
};
export default ProductPage;
