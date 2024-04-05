import styled, { css } from "styled-components";

export const SearchingResults = styled.div`
  padding: 35px 45px 100px;
  overflow: hidden;
  background-color: #fff;
`;

export const SearchingContent = styled.div`
  display: flex;
  flex-direction: row;
  margin-top: 10px;

  ${({ theme }) => css`
    ${theme.responsive.isDesktopBS &&
    css`
      margin: 10px auto 0;
      max-width: 1921px;
    `}
  `}
`;

export const FilterContainer = styled.div`
  margin-right: 20px;
`;

export const BreadCrumbs = styled.div`
  margin-bottom: 50px;
  color: #8f8f8f;
  font-size: 16px;
  line-height: 26px;
  font-family: Roboto;
`;

export const Span = styled.span`
  color: #11171f;
`;

export const PaginationContainer = styled.div`
  display: flex;
  flex-direction: column;
  margin: 0 auto;
`;
