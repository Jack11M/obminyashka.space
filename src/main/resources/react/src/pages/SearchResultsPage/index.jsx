import { TitleBigBlue } from 'components/common';
import { ProductCard } from 'components/item-card';
import { Filtration } from 'components/common/filtration';
import { getTranslatedText } from 'components/local/localization';

import * as Styles from './styles';

const SearchResultsPage = () => {
  return (
    <Styles.SearchingResults>
      <Styles.SearchingContent>
        <Styles.FilterContainer>
          <Styles.BreadCrumbs>
            {getTranslatedText('filterPage.home')}/
            <Styles.Span>
              {getTranslatedText('filterPage.searchResults')}
            </Styles.Span>
          </Styles.BreadCrumbs>

          <Filtration />
        </Styles.FilterContainer>

        <div>
          <TitleBigBlue text={getTranslatedText('filterPage.searchResults')} />

          <Styles.CardsContainer>
            <ProductCard
              city="Харьков"
              picture="https://static.toiimg.com/photo/72975551.cms"
              text="Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет"
            />

            <ProductCard
              city="Днепр"
              picture="https://static.toiimg.com/photo/72975551.cms"
              text="Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет"
            />

            <ProductCard
              city="Харьков"
              picture="https://static.toiimg.com/photo/72975551.cms"
              text="Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет"
            />

            <ProductCard
              city="Харьков"
              picture="https://static.toiimg.com/photo/72975551.cms"
              text="Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет"
            />

            <ProductCard
              city="Харьков"
              picture="https://static.toiimg.com/photo/72975551.cms"
              text="Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет"
            />

            <ProductCard
              city="Харьков"
              picture="https://static.toiimg.com/photo/72975551.cms"
              text="Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет"
            />

            <ProductCard
              city="Харьков"
              picture="https://static.toiimg.com/photo/72975551.cms"
              text="Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет"
            />

            <ProductCard
              city="Харьков"
              picture="https://static.toiimg.com/photo/72975551.cms"
              text="Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет"
            />

            <ProductCard
              city="Харьков"
              picture="https://static.toiimg.com/photo/72975551.cms"
              text="Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет"
            />

            <ProductCard
              city="Харьков"
              picture="https://static.toiimg.com/photo/72975551.cms"
              text="Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет"
            />

            <ProductCard
              city="Харьков"
              picture="https://static.toiimg.com/photo/72975551.cms"
              text="Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет"
            />

            <ProductCard
              city="Харьков"
              picture="https://static.toiimg.com/photo/72975551.cms"
              text="Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет"
            />

            <ProductCard
              city="Харьков"
              picture="https://static.toiimg.com/photo/72975551.cms"
              text="Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет"
            />

            <ProductCard
              city="Харьков"
              picture="https://static.toiimg.com/photo/72975551.cms"
              text="Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет"
            />

            <ProductCard
              city="Харьков"
              picture="https://static.toiimg.com/photo/72975551.cms"
              text="Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет"
            />
          </Styles.CardsContainer>
        </div>
      </Styles.SearchingContent>
    </Styles.SearchingResults>
  );
};

export default SearchResultsPage;
