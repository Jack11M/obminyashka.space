import styled, { css } from "styled-components";

export const Main = styled.main`
  background-color: #f2f2f2;
`;

export const Container = styled.div`
  display: flex;
  flex-direction: column;
  gap: 50px 0;
  padding: 0 15px 50px;
  margin: 0 auto;
  max-width: 1845px;
  background-color: #f2f2f2;
  overflow: hidden;

  ${({ theme }) => css`
    ${theme.responsive.isTablet &&
    css`
      gap: 70px 0;
      padding: 0 40px 70px;
    `}

    ${theme.responsive.isDesktop &&
    css`
      gap: 130px 0;
      padding: 0 45px 190px;
    `}
  `}
`;
