import { useState } from 'react';
import Pagination from 'rc-pagination';

import * as Styles from './styles';

const Paginate = () => {
  const [current, setCurrent] = useState(1);

  const onChange = (page) => {
    console.log(page);
    setCurrent(page);
  };

  return (
    <Styles.StylesForPagination>
      <Pagination
        onChange={onChange}
        current={current}
        total={80}
        showLessItems
        showTitle={false}
      />
    </Styles.StylesForPagination>
  );
};

export { Paginate };
