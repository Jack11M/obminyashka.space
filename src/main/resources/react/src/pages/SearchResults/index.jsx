import { useEffect, useState, useMemo } from 'react';
import { showMessage } from 'hooks';
import Pagination from 'rc-pagination';
import { useNavigate } from 'react-router-dom';

import api from 'REST/Resources';
import * as Icon from 'assets/icons';
import { route } from 'routes/routeConstants';
import { TitleBigBlue } from 'components/common';
import { ProductCard } from 'components/item-card';
// import { Paginate } from 'components/common/paginate';
import { getCity } from 'Utils/getLocationProperties';
import { Filtration } from 'components/common/filtration';
import { getTranslatedText } from 'components/local/localization';

import * as Styles from './styles';

const SearchResults = () => {
  const navigate = useNavigate();
  const [adv, setAdv] = useState({});

  const getData = async (page) => {
    try {
      const response = await api.search.getSearch('стол большой', page - 1, 2);
      setAdv(response);
    } catch (error) {
      showMessage(error.response?.data ?? error.message);
    }
  };

  useEffect(() => {
    getData(1);
  }, []);

  const pageNumber = useMemo(() => {
    if (adv.pageable) {
      return adv.pageable.pageNumber + 1;
    }
    return 1;
  }, [adv.pageable]);

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

          <Styles.StylesForPagination>
            <Styles.Container>
              {adv.content?.map((item) => (
                <ProductCard
                  text={item.title}
                  key={item.advertisementId}
                  city={getCity(item.location)}
                  picture={`data:image/jpeg;base64, ${item.image}`}
                  clickOnButton={() => moveToProductPage(item.advertisementId)}
                />
              ))}
            </Styles.Container>

            <Pagination
              showLessItems
              showTitle={false}
              onChange={getData}
              current={pageNumber}
              nextIcon={<Icon.Next />}
              prevIcon={<Icon.Prev />}
              total={adv.totalElements}
              pageSize={adv.pageable?.pageSize ?? 1}
            />
          </Styles.StylesForPagination>
        </div>
      </Styles.SearchingContent>
    </Styles.SearchingResults>
  );
};

export default SearchResults;
