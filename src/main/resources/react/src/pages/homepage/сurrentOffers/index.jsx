import { useState, useEffect } from 'react';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import api from 'REST/Resources';
import { getLang } from 'store/auth/slice';
import { route } from 'routes/routeConstants';
import ProductCard from 'components/item-card';
import noPhotos from 'assets/img/showAdv/noPhoto.svg';
import TitleBigBlue from 'components/common/title_Big_Blue';
import { getTranslatedText } from 'components/local/localization';

import * as Styles from './styles';

const CurrentOffers = () => {
  const navigate = useNavigate();
  const lang = useSelector(getLang);

  const [offers, setOffers] = useState([]);

  useEffect(() => {
    api.fetchHome
      .getCurrentOffers()
      .then(({ data }) => {
        if (Array.isArray(data)) setOffers(data);
      })
      .catch((e) => {
        console.log(e.response);
      });
  }, []);

  const moveToProductPage = (id) => {
    navigate(route.productPage.replace(':id', id));
  };

  return (
    <Styles.ProductSection>
      <Styles.ProductHeader>
        <TitleBigBlue text={getTranslatedText('mainPage.blueText', lang)} />
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

export default CurrentOffers;
