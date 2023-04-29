import { useSelector } from 'react-redux';
import { useLocation, useParams } from 'react-router-dom';
import { useCallback, useEffect, useMemo, useState } from 'react';
import {
  Title,
  BackButton,
  showMessage,
  ProductPostData,
  ProductOwnerData,
  ProductDescription,
} from 'obminyashka-components';

import api from 'src/REST/Resources';
import { enumAge } from 'src/config/ENUM';
import { getErrorMessage } from 'src/Utils/error';
import { getAuthLang } from 'src/store/auth/slice';
import { getProfile } from 'src/store/profile/slice';
import { getCity } from 'src/Utils/getLocationProperties';
import { getTranslatedText } from 'src/components/local/localization';

import { getDate } from './helpers';
import { ProductOffers } from './ProductOffers';
import { ProductPhotoCarousel } from './ProductPhotoCarousel';

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
    if (state) return profile?.avatarImage;
    return null;
  }, [state, product.ownerAvatar]);

  const phone = useMemo(() => {
    if (product.phone) return product.phone;
    if (state) return profile?.phones[0]?.phoneNumber;
    return '';
  }, [state, profile?.phones, product?.phone]);

  const name = useMemo(() => {
    if (product.ownerName) return product.ownerName;
    if (state) return profile?.username;
    return '';
  }, [state, profile?.username, product.ownerName]);

  const setPreviewData = useCallback(async () => {
    try {
      const locationValue = await api.product.getLocation(state.currentLocation.id);

      setWishes(state.wishes);
      setPhotos(state.photos);
      setProduct(state.product);
      setCategory(state.category);
      setSubcategory(state.subcategory);
      setCurrentLocation(locationValue);
    } catch (e) {
      showMessage.error(getErrorMessage(e));
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
      setCategory(categoryValue);
      setSubcategory(subcategoryValue);
      setCurrentLocation(locationValue);
      setWishes(wishesToExchange?.split(','));
      setPhotos(
        images.map((photo) => ({
          ...photo,
          resource: `data:image/jpeg;base64,${photo.resource}`,
        }))
      );
    } catch (e) {
      showMessage.error(getErrorMessage(e));
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
          <BackButton style={{ marginBottom: 16 }} text={getTranslatedText('button.back')} />

          <BreadCrumbs>
            {getTranslatedText('product.categories')} /&nbsp;
            {getTranslatedText(`categories.${category?.name}`)} /&nbsp;
            {getTranslatedText(`categories.${subcategory?.name}`)} /&nbsp;
            <Span>{product.topic}</Span>
          </BreadCrumbs>

          <ProductPageInner>
            <CarouselAndDescription>
              <ProductPhotoCarousel photos={photos} />
              <ProductDescription title={product.topic} description={product.description} />
            </CarouselAndDescription>

            <OwnerAndPost>
              <ProductOwnerData
                name={name}
                phone={phone}
                avatar={avatar}
                city={getCity(currentLocation)}
                date={product.createdDate || getDate(lang)}
                dateText={`${getTranslatedText('product.dateOfAdv')}:`}
                cityText={`${getTranslatedText('product.cityOfAdv')}:`}
                phoneText={`${getTranslatedText('product.phoneOfAdv')}:`}
              />

              <ProductPostData
                lang={lang}
                wishes={wishes}
                title={product.topic}
                readyForOffers={product.readyForOffers}
                size={product.size || product.sizeValue}
                age={enumAge[product.age] || product.age}
                buttonText={getTranslatedText('product.button')}
                translatedTextAge={getTranslatedText('product.age')}
                translatedTextSize={getTranslatedText('product.size')}
                translatedTextGender={getTranslatedText('product.sex')}
                translatedTextSeason={getTranslatedText('product.season')}
                gender={getTranslatedText(`genderEnum.${product.gender}`)}
                season={getTranslatedText(`seasonEnum.${product.season}`)}
                translatedTextCheckInUl={getTranslatedText('product.checkInUl')}
                translatedTextChangesTo={getTranslatedText('product.changesTo')}
                translatedTextDescription={getTranslatedText('product.description')}
              />
            </OwnerAndPost>
          </ProductPageInner>
        </ProductPageContainer>
      </TopSection>

      <BottomSection>
        <ProductPageContainer>
          <ProductPageInner>
            <SectionHeading>
              <Title text={getTranslatedText('product.blueTitle')} />
            </SectionHeading>

            <ProductOffers />
          </ProductPageInner>
        </ProductPageContainer>
      </BottomSection>
    </>
  );
};

export { ProductPage };
