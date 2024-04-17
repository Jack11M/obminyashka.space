import styled, { css } from "styled-components";

export const SearchingResults = styled.div`
  padding: 35px 0 180px;
  overflow: hidden;
  background-color: #fff;

  ${({ theme }) => css`
    ${theme.responsive.isDesktop &&
    css`
      padding: 35px 45px 180px;
    `}
  `}
`;

export const MobileButtonContainer = styled.div`
  display: flex;
  justify-content: center;
  margin-bottom: 50px;
`;

export const TabletButtonContainer = styled.div`
  margin-bottom: 30px;
  height: 30px;
  width: fit-content;
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
  display: flex;
  margin-bottom: 30px;
  color: #8f8f8f;
  font-size: 16px;
  line-height: 26px;
  font-family: Roboto;

  ${({ theme }) => css`
    ${theme.responsive.isDesktop &&
    css`
      margin-bottom: 50px;
    `}
  `}
`;

export const Span = styled.span`
  color: #11171f;
`;

export const PaginationContainer = styled.div`
  position: relative;
  display: flex;
  flex-direction: column;
  margin: 0 auto;
`;

export const ToUp = styled.div`
  position: absolute;
  display: flex;
  justify-content: center;
  align-items: center;
  right: 0;
  bottom: -85px;
  height: 50px;
  width: 50px;
  border-radius: 50%;
  box-shadow: 0px 4px 15px 0px rgba(126, 205, 228, 0.5);
  cursor: pointer;
`;

export const Image = styled.div`
  display: inline-block;
  padding: 7px;
  margin-top: 8px;
  border: solid ${({ theme }) => theme.colors.categoryFilter.border};
  border-width: 0 3px 3px 0;
  transform: rotate(-135deg);
  -webkit-transform: rotate(-135deg);
`;
