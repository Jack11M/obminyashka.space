import { Title, ProductCard, Images } from 'obminyashka-components';

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
          picture={Images.lot3}
          inboxMessage={12}
          buttonText={getTranslatedText('button.look')}
          text="Велосипед ну очень крутой. просто не реально крутой для девочки 5 лет"
        />

        <ProductCard
          city="Львов"
          picture={Images.lot3}
          inboxMessage={222}
          text="Велосипед для девочки 5 лет"
          buttonText={getTranslatedText('button.look')}
        />

        <ProductCard
          city="Киев"
          picture={Images.lot3}
          inboxMessage={50}
          text="Велосипед для девочки 5 лет"
          buttonText={getTranslatedText('button.look')}
        />

        <ProductCard
          city="Ужгород"
          picture={Images.lot3}
          inboxMessage={12}
          text="Велосипед для девочки 5 лет"
          buttonText={getTranslatedText('button.look')}
        />

        <ProductCard
          city="Киев"
          picture={Images.lot3}
          inboxMessage={5}
          text="Велосипед для девочки 5 лет"
          buttonText={getTranslatedText('button.look')}
        />

        <ProductCard
          city="Киев"
          picture={Images.lot3}
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
          picture={Images.lot3}
          city="Харьков"
          inboxMessage={3}
          isFavorite={isFavorite}
          buttonText={getTranslatedText('button.look')}
          text="Велосипед ну очень крутой. просто не реально крутой для девочки 5 лет"
        />

        <ProductCard
          picture={Images.lot3}
          city="Харьков"
          text="Велосипед"
          inboxMessage={10}
          buttonText={getTranslatedText('button.look')}
        />

        <ProductCard
          picture={Images.lot3}
          inboxMessage={25}
          city="Ивано-Франковск"
          isFavorite={isFavorite}
          text="Велосипед для девочки 5 лет"
          buttonText={getTranslatedText('button.look')}
        />

        <ProductCard
          picture={Images.lot3}
          city="Харьков"
          inboxMessage={3}
          buttonText={getTranslatedText('button.look')}
          text="Велосипед ну очень крутой. просто не реально крутой для девочки 5 лет"
        />

        <ProductCard
          picture={Images.lot3}
          city="Харьков"
          text="Велосипед"
          inboxMessage={10}
          isFavorite={isFavorite}
          buttonText={getTranslatedText('button.look')}
        />

        <ProductCard
          picture={Images.lot3}
          inboxMessage={25}
          city="Ивано-Франковск"
          text="Велосипед для девочки 5 лет"
          buttonText={getTranslatedText('button.look')}
        />

        <ProductCard
          picture={Images.lot3}
          city="Харьков"
          inboxMessage={3}
          isFavorite={isFavorite}
          buttonText={getTranslatedText('button.look')}
          text="Велосипед ну очень крутой. просто не реально крутой для девочки 5 лет"
        />

        <ProductCard
          picture={Images.lot3}
          city="Харьков"
          text="Велосипед"
          inboxMessage={10}
          buttonText={getTranslatedText('button.look')}
        />

        <ProductCard
          picture={Images.lot3}
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
