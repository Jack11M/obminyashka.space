import { ProductCard } from 'obminyashka-components';

import { getTranslatedText } from 'components/local/localization';

import { OffersContainer } from './styles';

const ProductOffers = () => (
  <OffersContainer>
    <ProductCard
      buttonText={getTranslatedText('button.look')}
      city="Харьков"
      picture="https://static.toiimg.com/photo/72975551.cms"
      text="Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет"
    />

    <ProductCard
      buttonText={getTranslatedText('button.look')}
      city="Харьков"
      picture="https://static.toiimg.com/photo/72975551.cms"
      text="Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет"
    />

    <ProductCard
      buttonText={getTranslatedText('button.look')}
      city="Харьков"
      picture="https://static.toiimg.com/photo/72975551.cms"
      text="Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет"
    />

    <ProductCard
      buttonText={getTranslatedText('button.look')}
      city="Харьков"
      picture="https://static.toiimg.com/photo/72975551.cms"
      text="Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет"
    />
  </OffersContainer>
);
export default ProductOffers;
