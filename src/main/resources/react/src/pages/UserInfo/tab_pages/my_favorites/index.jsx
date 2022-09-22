import React from 'react';

import { TitleBigBlue } from 'components/common';
import { ProductCard } from 'components/item-card';
import { getTranslatedText } from 'components/local/localization';

import * as Styles from './styles';

const MyFavorites = () => {
  const isFavorite = true;

  return (
    <section>
      <TitleBigBlue
        style={{ margin: '65px 0 40px' }}
        text={getTranslatedText('favorites.FeaturedAds')}
      />

      <Styles.CardsContainer>
        <ProductCard
          city="Харьков"
          isFavorite={isFavorite}
          picture="https://static.toiimg.com/photo/72975551.cms"
          text="Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет"
        />

        <ProductCard
          city="Харьков"
          isFavorite={isFavorite}
          picture="https://static.toiimg.com/photo/72975551.cms"
          text="Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет"
        />

        <ProductCard
          city="Харьков"
          isFavorite={isFavorite}
          picture="https://static.toiimg.com/photo/72975551.cms"
          text="Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет"
        />

        <ProductCard
          city="Харьков"
          isFavorite={isFavorite}
          picture="https://static.toiimg.com/photo/72975551.cms"
          text="Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет"
        />

        <ProductCard
          city="Харьков"
          isFavorite={isFavorite}
          picture="https://static.toiimg.com/photo/72975551.cms"
          text="Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет"
        />

        <ProductCard
          city="Харьков"
          isFavorite={isFavorite}
          picture="https://static.toiimg.com/photo/72975551.cms"
          text="Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет"
        />
      </Styles.CardsContainer>
    </section>
  );
};

export default React.memo(MyFavorites);
