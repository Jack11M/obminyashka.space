import lot3 from 'assets/img/cards/lot3.jpg';
import ProductCard from 'components/item-card';
import { TitleBigBlue } from 'components/common';
import mommy from 'assets/img/mama_1_04232631 1.png';

import './myActyvity.scss';

const MyActivity = () => {
  const isFavorite = true;
  return (
    <div>
      <TitleBigBlue whatClass="incoming__replies-text" text="Входящие ответы" />
      <div className="block-of-cards">
        <ProductCard
          city="Харьков"
          text="Велосипед ну очень крутой. просто не реально крутой для девочки 5 лет"
          picture={lot3}
          inboxMessage={12}
        />
        <ProductCard
          city="Львов"
          text="Велосипед для девочки 5 лет"
          picture={lot3}
          inboxMessage={222}
          avatar={mommy}
        />
        <ProductCard
          city="Киев"
          text="Велосипед для девочки 5 лет"
          picture={lot3}
          inboxMessage={50}
        />
        <ProductCard
          city="Ужгород"
          text="Велосипед для девочки 5 лет"
          picture={lot3}
          inboxMessage={12}
          avatar={mommy}
        />
        <ProductCard
          city="Киев"
          text="Велосипед для девочки 5 лет"
          picture={lot3}
          inboxMessage={5}
        />
        <ProductCard
          city="Киев"
          text="Велосипед для девочки 5 лет"
          picture={lot3}
          inboxMessage={33}
          avatar={mommy}
        />
      </div>

      <TitleBigBlue
        whatClass="outgoing__replies-text"
        text="Исходящие ответы"
      />
      <div className="block-of-cards">
        <ProductCard
          isFavorite={isFavorite}
          picture={lot3}
          city="Харьков"
          text="Велосипед ну очень крутой. просто не реально крутой для девочки 5 лет"
          inboxMessage={3}
        />
        <ProductCard
          picture={lot3}
          city="Харьков"
          text="Велосипед"
          inboxMessage={10}
        />
        <ProductCard
          isFavorite={isFavorite}
          picture={lot3}
          city="Ивано-Франковск"
          text="Велосипед для девочки 5 лет"
          inboxMessage={25}
        />
        <ProductCard
          picture={lot3}
          city="Харьков"
          text="Велосипед ну очень крутой. просто не реально крутой для девочки 5 лет"
          inboxMessage={3}
        />
        <ProductCard
          isFavorite={isFavorite}
          picture={lot3}
          city="Харьков"
          text="Велосипед"
          inboxMessage={10}
        />
        <ProductCard
          picture={lot3}
          city="Ивано-Франковск"
          text="Велосипед для девочки 5 лет"
          inboxMessage={25}
        />
        <ProductCard
          isFavorite={isFavorite}
          picture={lot3}
          city="Харьков"
          text="Велосипед ну очень крутой. просто не реально крутой для девочки 5 лет"
          inboxMessage={3}
        />
        <ProductCard
          picture={lot3}
          city="Харьков"
          text="Велосипед"
          inboxMessage={10}
        />
        <ProductCard
          isFavorite={isFavorite}
          picture={lot3}
          city="Ивано-Франковск"
          text="Велосипед для девочки 5 лет"
          inboxMessage={25}
        />
      </div>
    </div>
  );
};

export default MyActivity;
