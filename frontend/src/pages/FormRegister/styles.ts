import styled, { css, keyframes } from "styled-components";

export const sunMove = keyframes`
  0% {
    transform: rotate(0) translate(0,0);
  }

  33% {
    transform: rotate(-2deg) translate(0,-5px);
  }

  66% {
    transform: rotate(2deg) translate(5px,-5px);
  }

  100% {
    transform: rotate(0) translate(0,0);
  }
`;

export const Main = styled.div`
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
`;

export const Form = styled.div<{ variant?: number }>`
  position: relative;
  display: flex;
  flex-direction: column;
  margin: 30px 15px 40px;
  width: 100%;
  max-width: 806px;
  background-color: ${({ theme }) => theme.colors.colorPopup};
  border-radius: 22px;

  ${({ theme, variant }) => css`
    ${theme.responsive.isTablet &&
    css`
      margin: 85px 30px 85px;
      box-shadow: 0px 4px 40px 0px rgba(89, 102, 116, 0.25);

      ${variant === 1 &&
      css`
        margin: 60px 30px 60px;
      `}
    `}

    ${theme.responsive.isDesktop &&
    css`
      ${variant === 0 &&
      css`
        margin: 300px 0 112px;
      `}

      ${variant === 1 &&
      css`
        margin: 117px 0 112px;
      `}
    `}
  `}
`;

export const NavBarContainer = styled.div`
  position: relative;
  background-color: ${({ theme }) => theme.colors.colorPopup};
  border-radius: 22px;
  z-index: 2;
`;

export const SunLogin = styled.img`
  position: absolute;
  top: 12%;
  width: 240px;
  height: 240px;
  transform: translate(-50%, -5%);
  animation: ${sunMove} 2.5s infinite;
`;
