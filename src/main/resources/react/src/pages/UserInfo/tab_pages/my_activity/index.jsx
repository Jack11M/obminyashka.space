import lot3 from 'assets/img/cards/lot3.jpg';
import { TitleBigBlue } from 'components/common';
import { ProductCard } from 'components/item-card';
import mommy from 'assets/img/mama_1_04232631 1.png';
import { getTranslatedText } from 'components/local/localization';

import * as Styles from './styles';

const MyActivity = () => {
  const isFavorite = true;

  return (
    <>
      <TitleBigBlue
        style={{ margin: '65px 0 40px' }}
        text={getTranslatedText('ownActivity.incomingReplies')}
      />

      <Styles.CardsContainer>
        <ProductCard
          city="Харьков"
          picture={lot3}
          inboxMessage={12}
          text="Велосипед ну очень крутой. просто не реально крутой для девочки 5 лет"
        />

        <ProductCard
          city="Львов"
          picture={lot3}
          avatar="https://t4.ftcdn.net/jpg/02/43/87/41/360_F_243874126_YLSIGaDgoNzS91Xdbg1IVpiwXeeZSXdr.jpg"
          inboxMessage={222}
          text="Велосипед для девочки 5 лет"
        />

        <ProductCard
          city="Киев"
          picture={lot3}
          inboxMessage={50}
          text="Велосипед для девочки 5 лет"
        />

        <ProductCard
          city="Ужгород"
          picture={lot3}
          avatar={mommy}
          inboxMessage={12}
          text="Велосипед для девочки 5 лет"
        />

        <ProductCard
          city="Киев"
          picture={lot3}
          inboxMessage={5}
          text="Велосипед для девочки 5 лет"
        />

        <ProductCard
          city="Киев"
          picture={lot3}
          avatar={mommy}
          inboxMessage={33}
          text="Велосипед для девочки 5 лет"
        />
      </Styles.CardsContainer>

      <TitleBigBlue
        style={{ margin: '83px 0 40px' }}
        text={getTranslatedText('ownActivity.outgoingReplies')}
      />

      <Styles.StylizedCardBlock>
        <ProductCard
          picture={lot3}
          city="Харьков"
          inboxMessage={3}
          isFavorite={isFavorite}
          text="Велосипед ну очень крутой. просто не реально крутой для девочки 5 лет"
        />

        <ProductCard
          picture={lot3}
          city="Харьков"
          text="Велосипед"
          inboxMessage={10}
        />

        <ProductCard
          picture={lot3}
          inboxMessage={25}
          city="Ивано-Франковск"
          isFavorite={isFavorite}
          text="Велосипед для девочки 5 лет"
        />

        <ProductCard
          picture={lot3}
          city="Харьков"
          inboxMessage={3}
          text="Велосипед ну очень крутой. просто не реально крутой для девочки 5 лет"
        />

        <ProductCard
          picture={lot3}
          city="Харьков"
          text="Велосипед"
          inboxMessage={10}
          isFavorite={isFavorite}
        />

        <ProductCard
          picture={lot3}
          inboxMessage={25}
          city="Ивано-Франковск"
          text="Велосипед для девочки 5 лет"
        />

        <ProductCard
          picture={lot3}
          city="Харьков"
          inboxMessage={3}
          isFavorite={isFavorite}
          text="Велосипед ну очень крутой. просто не реально крутой для девочки 5 лет"
        />

        <ProductCard
          picture={lot3}
          city="Харьков"
          text="Велосипед"
          inboxMessage={10}
        />

        <ProductCard
          picture={lot3}
          inboxMessage={25}
          city="Ивано-Франковск"
          isFavorite={isFavorite}
          text="Велосипед для девочки 5 лет"
        />
      </Styles.StylizedCardBlock>
    </>
  );
};

export default MyActivity;
