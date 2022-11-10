// import { useState } from 'react';
// import Pagination from 'rc-pagination';

// import * as Icon from 'assets/icons';

// import * as Styles from './styles';

// const Paginate = ({ children, pageSize, showLessItems = true }) => {
//   const [currentPage, setCurrentPage] = useState(0);
//   const [collection, setCollection] = useState(children.slice(0, pageSize));
//   console.log(collection);

//   const updatePage = (p) => {
//     setCurrentPage(p);
//     const to = pageSize * p;
//     const from = to - pageSize;
//     setCollection(children.slice(from, to));
//   };

//   return (
//     <Styles.StylesForPagination>
//       <Pagination
//         showTitle={false}
//         pageSize={pageSize}
//         current={currentPage}
//         onChange={updatePage}
//         total={children.length}
//         nextIcon={<Icon.Next />}
//         prevIcon={<Icon.Prev />}
//         showLessItems={showLessItems}
//       />

//       <Styles.Container>{collection}</Styles.Container>

//       <Pagination
//         showTitle={false}
//         pageSize={pageSize}
//         current={currentPage}
//         onChange={updatePage}
//         total={children.length}
//         nextIcon={<Icon.Next />}
//         prevIcon={<Icon.Prev />}
//         showLessItems={showLessItems}
//       />
//     </Styles.StylesForPagination>
//   );
// };

// export { Paginate };
