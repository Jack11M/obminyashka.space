import { TitleBigBlue } from 'components/common';
import { ProductCard } from 'components/item-card';
import { Filtration } from 'components/common/filtration';
import { getTranslatedText } from 'components/local/localization';

import { data } from './config';

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
            {data.map((item) => (
              <ProductCard
                key={item.id}
                city={item.city}
                text={item.text}
                picture={item.picture}
              />
            ))}
          </Styles.CardsContainer>
        </div>
      </Styles.SearchingContent>
    </Styles.SearchingResults>
  );
};

export default SearchResultsPage;
