import styled, { css } from "styled-components";

export const NavBarWrapper = styled.div`
  display: flex;
  margin-bottom: 36px;

  ${({ theme }) => css`
    ${theme.responsive.isTablet &&
    css`
      margin-bottom: 44px;
    `}
  `}
`;

export const Tab = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 46px;
  width: 50%;

  &.focus {
    border-bottom: 2px solid ${({ theme }) => theme.colors.auth.titleBottomLine};
  }
`;
