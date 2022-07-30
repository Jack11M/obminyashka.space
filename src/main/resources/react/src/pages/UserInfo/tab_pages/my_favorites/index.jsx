import React from 'react';

import ProductCard from 'components/item-card';
import TitleBigBlue from 'components/common/title_Big_Blue/title_Big_Blue';

import { CardsContainer } from './styles';

const MyFavorites = () => {
  const isFavorite = true;

  return (
    <section id="content3" className="tabs__content">
      <div className="content">
        <TitleBigBlue
          text="Избранные объявления"
          style={{ margin: '65px 0 40px' }}
        />

        <CardsContainer>
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
        </CardsContainer>
      </div>
    </section>
  );
};

export default React.memo(MyFavorites);
