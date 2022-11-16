import { useContext, useEffect, useState } from 'react';
import { showMessage } from 'hooks';
import { useNavigate } from 'react-router-dom';

import api from 'REST/Resources';
import { route } from 'routes/routeConstants';
import { ProductCard } from 'components/item-card';
import { getCity } from 'Utils/getLocationProperties';
import { getTranslatedText } from 'components/local/localization';
import {
  Filtration,
  TitleBigBlue,
  SearchContext,
  PagePagination,
} from 'components/common';

import * as Styles from './styles';

const SearchResults = () => {
  const navigate = useNavigate();
  const { search, setSearch, isFetch, setIsFetch } = useContext(SearchContext);

  const [adv, setAdv] = useState({});

  const getAdv = async (page) => {
    try {
      const response = await api.search.getSearch(search, page - 1);
      setAdv(response);
    } catch (err) {
      showMessage(err.response?.data ?? err.message);
    } finally {
      setIsFetch(false);
    }
  };

  useEffect(() => {
    if (isFetch) {
      getAdv(1);
    }
  }, [isFetch]);

  useEffect(() => {
    getAdv(1);

    return () => {
      setSearch('');
    };
  }, []);

  const moveToProductPage = (id) => {
    navigate(route.productPage.replace(':id', id));
  };

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

          {adv.content && (
            <PagePagination
              onChange={getAdv}
              current={adv.number + 1}
              pageSize={adv?.size || 1}
              total={adv.totalElements}
            >
              {adv.content?.length > 0 &&
                adv.content.map((item) => (
                  <ProductCard
                    text={item.title}
                    key={item.advertisementId}
                    city={getCity(item.location)}
                    picture={`data:image/jpeg;base64,${item.image}`}
                    clickOnButton={() =>
                      moveToProductPage(item.advertisementId)
                    }
                  />
                ))}
            </PagePagination>
          )}
        </div>
      </Styles.SearchingContent>
    </Styles.SearchingResults>
  );
};

export { SearchResults };
