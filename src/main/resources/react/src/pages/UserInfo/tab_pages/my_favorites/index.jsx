import React from 'react';
import { Title, ProductCard } from '@wolshebnik/obminyashka-components';

import { getTranslatedText } from 'components/local/localization';

import * as Styles from './styles';
import { productData } from './config';

const MyFavorites = () => {
  return (
    <>
      <Title
        style={{ margin: '65px 0 40px' }}
        text={getTranslatedText('favorites.FeaturedAds')}
      />

      <Styles.CardsContainer>
        {productData.map((card, idx) => (
          <ProductCard
            city={card.city}
            text={card.text}
            picture={card.picture}
            isFavorite={card.isFavorite}
            key={String(card.city + idx)}
          />
        ))}
      </Styles.CardsContainer>
    </>
  );
};

export default React.memo(MyFavorites);
