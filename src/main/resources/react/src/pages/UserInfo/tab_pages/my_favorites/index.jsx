import React from 'react';

import ProductCard from 'components/item-card/ProductCard';
import TitleBigBlue from 'components/common/title_Big_Blue/title_Big_Blue';

import { CardsContainer } from './styles';

const MyFavorites = () => {
  const isFavorite = true;

  return (
    <section id="content3" className="tabs__content">
      <div className="content">
        <TitleBigBlue whatClass="myProfile-title" text="Избранные объявления" />

        <CardsContainer>
          <ProductCard
            city="Харьков"
            text="Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет"
            picture="https://static.toiimg.com/photo/72975551.cms"
            isFavorite={isFavorite}
          />

          <ProductCard
            city="Харьков"
            text="Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет"
            picture="https://static.toiimg.com/photo/72975551.cms"
            isFavorite={isFavorite}
          />

          <ProductCard
            city="Харьков"
            text="Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет"
            picture="https://static.toiimg.com/photo/72975551.cms"
            isFavorite={isFavorite}
          />

          <ProductCard
            city="Харьков"
            text="Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет"
            picture="https://static.toiimg.com/photo/72975551.cms"
            isFavorite={isFavorite}
          />

          <ProductCard
            city="Харьков"
            text="Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет"
            picture="https://static.toiimg.com/photo/72975551.cms"
            isFavorite={isFavorite}
          />

          <ProductCard
            city="Харьков"
            text="Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет"
            picture="https://static.toiimg.com/photo/72975551.cms"
            isFavorite={isFavorite}
          />
        </CardsContainer>
      </div>
    </section>
  );
};

export default React.memo(MyFavorites);
