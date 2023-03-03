import { Title, ProductCard } from 'obminyashka-components';

import lot3 from 'assets/img/cards/lot3.jpg';
import { getTranslatedText } from 'components/local/localization';

import * as Styles from './styles';

const MyActivity = () => {
  const isFavorite = true;

  return (
    <>
      <Title
        style={{ margin: '65px 0 40px' }}
        text={getTranslatedText('ownActivity.incomingReplies')}
      />

      <Styles.InputResponseContainer>
        <ProductCard
          city="Харьков"
          picture={lot3}
          inboxMessage={12}
          buttonText={getTranslatedText('button.look')}
          text="Велосипед ну очень крутой. просто не реально крутой для девочки 5 лет"
        />

        <ProductCard
          city="Львов"
          picture={lot3}
          inboxMessage={222}
          text="Велосипед для девочки 5 лет"
          buttonText={getTranslatedText('button.look')}
        />

        <ProductCard
          city="Киев"
          picture={lot3}
          inboxMessage={50}
          text="Велосипед для девочки 5 лет"
          buttonText={getTranslatedText('button.look')}
        />

        <ProductCard
          city="Ужгород"
          picture={lot3}
          inboxMessage={12}
          text="Велосипед для девочки 5 лет"
          buttonText={getTranslatedText('button.look')}
        />

        <ProductCard
          city="Киев"
          picture={lot3}
          inboxMessage={5}
          text="Велосипед для девочки 5 лет"
          buttonText={getTranslatedText('button.look')}
        />

        <ProductCard
          city="Киев"
          picture={lot3}
          inboxMessage={33}
          text="Велосипед для девочки 5 лет"
          buttonText={getTranslatedText('button.look')}
        />
      </Styles.InputResponseContainer>

      <Title
        style={{ margin: '83px 0 40px' }}
        text={getTranslatedText('ownActivity.outgoingReplies')}
      />

      <Styles.OutputResponseContainer>
        <ProductCard
          picture={lot3}
          city="Харьков"
          inboxMessage={3}
          isFavorite={isFavorite}
          buttonText={getTranslatedText('button.look')}
          text="Велосипед ну очень крутой. просто не реально крутой для девочки 5 лет"
        />

        <ProductCard
          picture={lot3}
          city="Харьков"
          text="Велосипед"
          inboxMessage={10}
          buttonText={getTranslatedText('button.look')}
        />

        <ProductCard
          picture={lot3}
          inboxMessage={25}
          city="Ивано-Франковск"
          isFavorite={isFavorite}
          text="Велосипед для девочки 5 лет"
          buttonText={getTranslatedText('button.look')}
        />

        <ProductCard
          picture={lot3}
          city="Харьков"
          inboxMessage={3}
          buttonText={getTranslatedText('button.look')}
          text="Велосипед ну очень крутой. просто не реально крутой для девочки 5 лет"
        />

        <ProductCard
          picture={lot3}
          city="Харьков"
          text="Велосипед"
          inboxMessage={10}
          isFavorite={isFavorite}
          buttonText={getTranslatedText('button.look')}
        />

        <ProductCard
          picture={lot3}
          inboxMessage={25}
          city="Ивано-Франковск"
          text="Велосипед для девочки 5 лет"
          buttonText={getTranslatedText('button.look')}
        />

        <ProductCard
          picture={lot3}
          city="Харьков"
          inboxMessage={3}
          isFavorite={isFavorite}
          buttonText={getTranslatedText('button.look')}
          text="Велосипед ну очень крутой. просто не реально крутой для девочки 5 лет"
        />

        <ProductCard
          picture={lot3}
          city="Харьков"
          text="Велосипед"
          inboxMessage={10}
          buttonText={getTranslatedText('button.look')}
        />

        <ProductCard
          picture={lot3}
          inboxMessage={25}
          city="Ивано-Франковск"
          isFavorite={isFavorite}
          text="Велосипед для девочки 5 лет"
          buttonText={getTranslatedText('button.look')}
        />
      </Styles.OutputResponseContainer>
    </>
  );
};

export default MyActivity;
