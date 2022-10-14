import { TitleBigBlue } from 'components/common';
import { ProductCard } from 'components/item-card';
import { Paginate } from 'components/common/Paginate';
import { Filtration } from 'components/common/filtration';
import { getTranslatedText } from 'components/local/localization';

import { allData } from './config';

import * as Styles from './styles';

const SearchResults = () => {
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

          <Paginate>
            {allData.map((item) => (
              <ProductCard
                key={item.id}
                city={item.city}
                text={item.text}
                picture={item.picture}
              />
            ))}
          </Paginate>
        </div>
      </Styles.SearchingContent>
    </Styles.SearchingResults>
  );
};

export default SearchResults;
