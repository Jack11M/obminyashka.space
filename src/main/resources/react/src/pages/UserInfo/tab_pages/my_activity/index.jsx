import lot3 from 'assets/img/cards/lot3.jpg';
import ProductCard from 'components/item-card';
import mommy from 'assets/img/mama_1_04232631 1.png';
import TitleBigBlue from 'components/common/title_Big_Blue';

import { CardBlock, Container, StyledCardBlock } from './styles';

const MyActivity = () => {
  const isFavorite = true;

  return (
    <Container>
      <TitleBigBlue whatClass="incoming__replies-text" text="Входящие ответы" />

      <CardBlock>
        <ProductCard
          city="Харьков"
          picture={lot3}
          inboxMessage={12}
          text="Велосипед ну очень крутой. просто не реально крутой для девочки 5 лет"
        />

        <ProductCard
          city="Львов"
          picture={lot3}
          avatar={mommy}
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
      </CardBlock>

      <TitleBigBlue
        text="Исходящие ответы"
        whatClass="outgoing__replies-text"
      />

      <StyledCardBlock>
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
      </StyledCardBlock>
    </Container>
  );
};

export default MyActivity;
