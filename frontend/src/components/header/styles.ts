import styled, { css } from 'styled-components';

export const Header = styled.header`
  position: relative;
  top: 0;
  left: 0;
  z-index: 1000;
`;

export const HeaderOverlay = styled.div`
  position: relative;

  ${({ theme }) => css`
    background: ${theme.colors.search.white};
  `}
`;

export const Container = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 15px;
  margin: auto;
  max-width: 1920px;
  height: 82px;

  ${({ theme }) => css`
    ${theme.responsive.isTablet &&
    css`
      padding: 0 40px;
      height: 85px;
    `}

    ${theme.responsive.isDesktop &&
    css`
      padding: 0 45px;
      height: 168px;
    `}
  `};
`;

export const BtnContainer = styled.div<{ width: number }>`
  width: 100%;
  max-width: 267px;

  ${({ width }) => css`
    ${width > 1562 &&
    css`
      max-width: 244px;
    `}

    ${width > 1720 &&
    css`
      max-width: 287px;
    `}

      ${width > 1920 &&
    css`
      max-width: 290px;
    `}
  `}
`;

export const LngAvatarContainer = styled.div<{ width: number }>`
  display: flex;
  align-items: center;
  gap: 5px;

  ${({ width }) => css`
    ${width > 1920 &&
    css`
      gap: 44px;
    `}
  `};
`;
