/* eslint-disable */
// @ts-nocheck
// TODO: fix typescript
import React from 'react';
import { Title, ProductCard } from 'obminyashka-components';

import { getTranslatedText } from 'src/components/local/localization';

import * as Styles from './styles';
import { productData } from './config';

const MyFavorites = () => {
  return (
    <>
      <Title style={{ margin: '65px 0 40px' }} text={getTranslatedText('favorites.FeaturedAds')} />

      <Styles.CardsContainer>
        {productData.map((card, idx) => (
          <ProductCard
            city={card.city}
            text={card.text}
            picture={card.picture}
            isFavorite={card.isFavorite}
            key={String(card.city + idx)}
            buttonText={getTranslatedText('button.look')}
          />
        ))}
      </Styles.CardsContainer>
    </>
  );
};

export default React.memo(MyFavorites);
