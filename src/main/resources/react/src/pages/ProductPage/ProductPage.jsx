import { useCallback, useEffect, useMemo, useState } from 'react';
import { toast } from 'react-toastify';
import { useSelector } from 'react-redux';
import { useLocation, useParams } from 'react-router-dom';

import api from 'REST/Resources';
import { enumAge } from 'config/ENUM';
import { getAuthLang } from 'store/auth/slice';
import { getErrorMessage } from 'Utils/error';
import { getProfile } from 'store/profile/slice';
import { BackButton, TitleBigBlue } from 'components/common';
import { getTranslatedText } from 'components/local/localization';

import ProductOffers from './ProductOffers';
import { getCity, getDate } from './helpers';
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
  const profile = useSelector(getProfile);

  const [photos, setPhotos] = useState([]);
  const [wishes, setWishes] = useState([]);
  const [product, setProduct] = useState({});
  const [category, setCategory] = useState({});
  const [subcategory, setSubcategory] = useState({});
  const [currentLocation, setCurrentLocation] = useState({});

  const { state } = location;

  const avatar = useMemo(() => {
    if (product.ownerAvatar) return product.ownerAvatar;
    if (state) return profile.avatarImage;
    return null;
  }, [state, product.ownerAvatar]);

  const phone = useMemo(() => {
    if (product.phone) return product.phone;
    if (state) return profile.phones[0]?.phoneNumber;
    return '';
  }, [state, profile.phones, product.phone]);

  const name = useMemo(() => {
    if (product.ownerName) return product.ownerName;
    if (state) return profile.username;
    return '';
  }, [state, profile.username, product.ownerName]);

  const setPreviewData = useCallback(async () => {
    try {
      const locationValue = await api.product.getLocation(
        state.currentLocation.id
      );

      setWishes(state.wishes);
      setPhotos(state.photos);
      setProduct(state.product);
      setCategory(state.category);
      setSubcategory(state.subcategory);
      setCurrentLocation(locationValue);
    } catch (e) {
      toast.error(getErrorMessage(e));
    }
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
      } = await api.product.getProduct(id);

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
            {getTranslatedText('product.categories')} /&nbsp;
            {getTranslatedText(`categories.${category?.name}`)} /&nbsp;
            {getTranslatedText(`categories.${subcategory?.name}`)} /&nbsp;
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
                name={name}
                phone={phone}
                avatar={avatar}
                city={getCity(currentLocation)}
                date={product.createdDate || getDate(lang)}
              />

              <ProductPostData
                wishes={wishes}
                title={product.topic}
                readyForOffers={product.readyForOffers}
                size={product.size || product.sizeValue}
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
