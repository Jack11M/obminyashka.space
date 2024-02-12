import styled, { css } from "styled-components";

export const ProductSection = styled.section`
  margin-top: 10px;
`;

export const ProductHeader = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;

  ${({ theme }) => css`
    ${theme.responsive.isTablet &&
    css`
      margin-bottom: 35px;
    `}

    ${theme.responsive.isDesktop &&
    css`
      margin-bottom: 70px;
    `}
  `}
`;

export const ProductListUl = styled.ul`
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  grid-gap: 20px;

  ${({ theme }) => css`
    ${theme.responsive.isMobileBG &&
    css`
      grid-template-columns: repeat(3, 1fr);
    `}

    ${theme.responsive.isTablet &&
    css`
      grid-template-columns: repeat(2, 1fr);
    `}
      
      ${theme.responsive.isTabletBG &&
    css`
      grid-template-columns: repeat(3, 1fr);
    `}
      
      ${theme.responsive.isDesktop &&
    css`
      grid-template-columns: repeat(4, 1fr);
    `}
      
      ${theme.responsive.isDesktopBS &&
    css`
      grid-template-columns: repeat(5, 1fr);
    `}
  `}
`;

export const ProductListLI = styled.div`
  margin: 0 auto;
`;
