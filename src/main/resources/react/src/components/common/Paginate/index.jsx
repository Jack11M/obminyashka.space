import { useState } from 'react';
import Pagination from 'rc-pagination';

import * as Icon from 'assets/icons';

import * as Styles from './styles';

const Paginate = ({ children, showLessItems = true }) => {
  const countPerPage = 2;
  const [currentPage, setCurrentPage] = useState(1);
  const [collection, setCollection] = useState(
    Object.assign(children.slice(0, countPerPage))
  );

  const updatePage = (p) => {
    setCurrentPage(p);
    const to = countPerPage * p;
    const from = to - countPerPage;
    setCollection(Object.assign(children.slice(from, to)));
  };

  return (
    <Styles.StylesForPagination>
      <Styles.Container>{collection}</Styles.Container>

      <Pagination
        showTitle={false}
        current={currentPage}
        onChange={updatePage}
        total={children.length}
        pageSize={countPerPage}
        nextIcon={<Icon.Next />}
        prevIcon={<Icon.Prev />}
        showLessItems={showLessItems}
      />
    </Styles.StylesForPagination>
  );
};

export { Paginate };
