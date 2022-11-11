import Pagination from 'rc-pagination';

import * as Icon from 'assets/icons';

import * as Styles from './styles';

const Paginate = ({ children, onChange, current, pageSize, total }) => {
  return (
    <Styles.StylesForPagination>
      <Styles.Container>{children}</Styles.Container>

      <Pagination
        showLessItems
        total={total}
        showTitle={false}
        current={current}
        onChange={onChange}
        pageSize={pageSize}
        nextIcon={<Icon.Next />}
        prevIcon={<Icon.Prev />}
      />
    </Styles.StylesForPagination>
  );
};

export { Paginate };
