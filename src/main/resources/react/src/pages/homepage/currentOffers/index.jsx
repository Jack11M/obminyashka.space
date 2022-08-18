import { useState, useEffect } from 'react';
import { toast } from 'react-toastify';
import { useNavigate } from 'react-router-dom';

import api from 'REST/Resources';
import { route } from 'routes/routeConstants';
import { getErrorMessage } from 'Utils/error';
import { TitleBigBlue } from 'components/common';
import { ProductCard } from 'components/item-card';
import noPhotos from 'assets/img/showAdv/noPhoto.svg';
import { getTranslatedText } from 'components/local/localization';

import * as Styles from './styles';

const CurrentOffers = () => {
  const navigate = useNavigate();

  const [offers, setOffers] = useState([]);

  useEffect(() => {
    api.fetchHome
      .getCurrentOffers()
      .then(({ data }) => {
        if (Array.isArray(data)) setOffers(data);
      })
      .catch((e) => {
        toast.error(getErrorMessage(e));
      });
  }, []);

  const moveToProductPage = (id) => {
    navigate(route.productPage.replace(':id', id));
  };

  return (
    <Styles.ProductSection>
      <Styles.ProductHeader>
        <TitleBigBlue text={getTranslatedText('mainPage.blueText')} />
      </Styles.ProductHeader>

      <Styles.ProductListUl>
        {offers.map((offer) => (
          <li key={offer.advertisementId}>
            <ProductCard
              margin="10px 8px"
              isFavorite={false}
              text={offer.title}
              city={offer.location.city || ''}
              clickOnButton={() => moveToProductPage(offer.advertisementId)}
              picture={
                offer.image ? `data:image/jpeg;base64,${offer.image}` : noPhotos
              }
            />
          </li>
        ))}
      </Styles.ProductListUl>
    </Styles.ProductSection>
  );
};

export { CurrentOffers };