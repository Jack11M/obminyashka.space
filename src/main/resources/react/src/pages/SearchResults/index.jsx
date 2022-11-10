import { useEffect, useState } from 'react';
import Pagination from 'rc-pagination';
import { showMessage } from 'hooks';

import api from 'REST/Resources';
import { TitleBigBlue } from 'components/common';
import * as Icon from 'assets/icons';
import { ProductCard } from 'components/item-card';
// import { Paginate } from 'components/common/paginate';
import { getCity } from 'Utils/getLocationProperties';
import { Filtration } from 'components/common/filtration';
import { getTranslatedText } from 'components/local/localization';

import * as Styles from './styles';

const SearchResults = () => {
  const [adv, setAdv] = useState([]);
  const [pageSize, setPageSize] = useState('');
  const [totalSize, setTotalSize] = useState('');
  const [currentPage, setCurrentPage] = useState('');

  useEffect(() => {
    const getAdv = async () => {
      const response = await api.search.getSearch('Штаны', 0, 6);
      setAdv(response.content);
      setPageSize(response.size);
      setCurrentPage(response.number + 1);
      setTotalSize(response.totalElements);
      console.log(response);
    };
    getAdv();
  }, []);

  // const fetchPageNumber = async (page) => {
  //   try {
  //     await api.search.getSearch('Штаны', page, 6);
  //     console.log(page);
  //   } catch (err) {
  //     showMessage(err.response?.data ?? err.message);
  //   }
  // };

  const updatePage = async (page) => {
    try {
      const newPageData = await api.search.getSearch('Штаны', page, 6);
      setAdv(newPageData.content);
      setCurrentPage(page);
      setPageSize(newPageData.size);
      setTotalSize(newPageData.totalElements);
      console.log(newPageData);
    } catch (err) {
      showMessage(err.response?.data ?? err.message);
    }
  };

  // const indexOfLastPage = currentPage + pageSize;
  // const indexOfFirstPage = indexOfLastPage + pageSize;
  // const currentPosts = adv.slice(indexOfFirstPage, indexOfLastPage);

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
              {adv.map((item) => (
                <ProductCard
                  text={item.title}
                  key={item.advertisementId}
                  city={getCity(item.location)}
                  picture={`data:image/jpeg;base64, ${item.image}`}
                />
              ))}
            </Styles.Container>

            <Pagination
              showTitle={false}
              total={totalSize}
              pageSize={pageSize}
              current={currentPage}
              onChange={updatePage}
              nextIcon={<Icon.Next />}
              prevIcon={<Icon.Prev />}
              showLessItems
            />
          </Styles.StylesForPagination>
        </div>
      </Styles.SearchingContent>
    </Styles.SearchingResults>
  );
};

export default SearchResults;
