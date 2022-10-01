import React from 'react';

import { TitleBigBlue } from 'components/common';
import { ProductCard } from 'components/item-card';
import { getTranslatedText } from 'components/local/localization';

import * as Styles from './styles';
import { productData } from './config';

const MyFavorites = () => {
  return (
    <>
      <TitleBigBlue
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
