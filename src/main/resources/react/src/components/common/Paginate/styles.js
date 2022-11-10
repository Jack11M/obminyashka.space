// import styled, { css } from 'styled-components';

// export const Container = styled.div`
//   display: flex;
//   flex-direction: row;
//   align-content: flex-start;
//   flex-wrap: wrap;
//   gap: 20px 13px;
//   margin: 50px 0 65px;
//   width: 910px;
// `;

// export const StylesForPagination = styled.div`
//   ${({ theme }) => css`
//     margin: 0;
//     padding: 0;
//     font-size: 22px;

//     ul,
//     ol {
//       display: flex;
//       justify-content: center;
//       align-items: center;
//       margin: 0;
//       padding: 0;
//       list-style: none;
//     }

//     .rc-pagination-item {
//       display: flex;
//       justify-content: center;
//       align-items: center;
//       width: 40px;
//       height: 40px;
//       text-align: center;
//       list-style: none;
//       border: 1px solid ${theme.colors.btnBlue};
//       border-radius: 50%;
//       color: ${theme.colors.btnBlue};
//       outline: 0;
//       cursor: pointer;

//       :focus,
//       :hover {
//         background-color: ${theme.colors.btnBlue};
//         color: ${theme.colors.white};
//       }

//       &-active {
//         color: ${theme.colors.white};
//         background-color: ${theme.colors.btnBlue};
//       }
//     }
//   `}

//   .rc-pagination-jump-prev,
//   .rc-pagination-jump-next {
//     ${({ theme }) => css`
//       outline: 0;

//       button {
//         width: 40px;
//         height: 40px;
//         padding-top: 12px;
//         border: 1px solid ${theme.colors.btnBlue};
//         border-radius: 50%;
//         color: ${theme.colors.btnBlue};
//         outline: 0;
//         cursor: pointer;

//         :hover {
//           background-color: ${theme.colors.btnBlue};
//           color: ${theme.colors.white};
//         }
//       }

//       button::after {
//         content: '•••';
//       }
//     `}
//   }

//   .rc-pagination-item,
//   .rc-pagination-prev,
//   .rc-pagination-jump-prev,
//   .rc-pagination-jump-next {
//     margin-right: 10px;
//   }

//   .rc-pagination-prev,
//   .rc-pagination-next {
//     display: flex;
//     cursor: pointer;
//   }

//   .rc-pagination-disabled {
//     opacity: 0;
//     pointer-events: none;
//   }
// `;
