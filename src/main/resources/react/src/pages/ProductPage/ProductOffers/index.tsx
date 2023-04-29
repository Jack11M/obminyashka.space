import { ProductCard } from 'obminyashka-components';

import { getTranslatedText } from 'src/components/local/localization';

import { OffersContainer } from './styles';

const ProductOffers = () => (
  <OffersContainer>
    <ProductCard
      city='Харьков'
      buttonText={getTranslatedText('button.look')}
      picture='https://static.toiimg.com/photo/72975551.cms'
      text='Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет'
    />

    <ProductCard
      city='Харьков'
      buttonText={getTranslatedText('button.look')}
      picture='https://static.toiimg.com/photo/72975551.cms'
      text='Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет'
    />

    <ProductCard
      city='Харьков'
      buttonText={getTranslatedText('button.look')}
      picture='https://static.toiimg.com/photo/72975551.cms'
      text='Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет'
    />

    <ProductCard
      city='Харьков'
      buttonText={getTranslatedText('button.look')}
      picture='https://static.toiimg.com/photo/72975551.cms'
      text='Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет'
    />
  </OffersContainer>
);

export { ProductOffers };
