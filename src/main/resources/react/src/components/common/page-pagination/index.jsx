import Pagination from 'rc-pagination';
import { Icon } from '@wolshebnik/obminyashka-components';

import * as Styles from './styles';

const PagePagination = ({ children, onChange, current, pageSize, total }) => {
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

export { PagePagination };
