import styled from 'styled-components';

import { PaginationArrowSvg } from 'assets/icons';

export const StylesForPagination = styled.div`
  margin: 0;
  padding: 0;
  font-size: 22px;

  ul,
  ol {
    display: flex;
    justify-content: center;
    margin: 0;
    padding: 0;
    list-style: none;
  }

  .rc-pagination-item {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 40px;
    height: 40px;
    text-align: center;
    vertical-align: middle;
    list-style: none;
    border: 1px solid #12b6ed;
    border-radius: 50%;
    color: #12b6ed;
    outline: 0;
    user-select: none;
    cursor: pointer;

    :focus,
    :hover {
      background-color: #12b6ed;
      color: white;
    }

    &-active {
      color: white;
      background-color: #12b6ed;
    }
  }

  .rc-pagination-jump-prev,
  .rc-pagination-jump-next {
    outline: 0;

    button {
      width: 40px;
      height: 40px;
      border: 1px solid #12b6ed;
      border-radius: 50%;
      color: #12b6ed;
      outline: 0;
      cursor: pointer;

      :hover {
        background-color: #12b6ed;
        color: white;
      }
    }

    button::after {
      content: '•••';
    }
  }

  .rc-pagination-item,
  .rc-pagination-prev,
  .rc-pagination-jump-prev,
  .rc-pagination-jump-next {
    margin-right: 10px;
  }

  .rc-pagination-item-link {
    width: 40px;
    height: 40px;
    color: #12b6ed;
  }

  .rc-pagination-prev button::before {
    content: url(${PaginationArrowSvg});
    transform: rotate(180deg);
    display: block;
  }

  .rc-pagination-next button::after {
    display: block;
    content: ' ';
    background-image: url({PaginationArrowSvg});
    background-size: 28px 28px;
    height: 28px;
    width: 28px;
  }
`;
