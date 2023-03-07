import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Icon, Title, ProductCard, showMessage } from 'obminyashka-components';

import api from 'REST/Resources';
import { route } from 'routes/routeConstants';
import { getErrorMessage } from 'Utils/error';
import { getCity } from 'Utils/getLocationProperties';
import { getTranslatedText } from 'components/local/localization';

import * as Styles from './styles';

const CurrentOffers = () => {
  const navigate = useNavigate();

  const [offers, setOffers] = useState([]);

  useEffect(() => {
    api.home
      .getCurrentOffers()
      .then(({ data }) => {
        if (Array.isArray(data)) setOffers(data);
      })
      .catch((e) => {
        showMessage.error(getErrorMessage(e));
      });
  }, []);

  const moveToProductPage = (id) => {
    navigate(route.productPage.replace(':id', id));
  };

  return (
    <Styles.ProductSection>
      <Styles.ProductHeader>
        <Title text={getTranslatedText('mainPage.blueText')} />
      </Styles.ProductHeader>

      <Styles.ProductListUl>
        {offers.map((offer) => (
          <li key={offer.advertisementId}>
            <ProductCard
              margin="10px 8px"
              text={offer.title}
              isFavorite={false}
              city={getCity(offer.location)}
              buttonText={getTranslatedText('button.look')}
              onClick={() => moveToProductPage(offer.advertisementId)}
              picture={
                offer.image ? (
                  `data:image/jpeg;base64,${offer.image}`
                ) : (
                  <Icon.NoPhoto />
                )
              }
            />
          </li>
        ))}
      </Styles.ProductListUl>
    </Styles.ProductSection>
  );
};

export { CurrentOffers };
