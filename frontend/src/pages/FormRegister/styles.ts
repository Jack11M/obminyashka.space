import styled, { css } from "styled-components";

import * as Animation from "./animation";

const timeAnimation = 100;

export const Main = styled.div`
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #97d7e3 18.83%, #39a5cf 100%);
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
  z-index: 2;

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

export const SunLogin = styled.img`
  position: absolute;
  top: 12%;
  width: 240px;
  height: 240px;
  animation: ${Animation.sunMove} 2.5s infinite;
  z-index: 1;
`;

export const CloudOne = styled.img`
  position: absolute;
  right: 29%;
  bottom: 4%;
  width: 212px;
  height: 109px;
  animation: ${Animation.cloudOne} ${timeAnimation}s linear infinite;
`;

export const CloudTwo = styled.img`
  position: absolute;
  right: 7%;
  bottom: 29%;
  width: 164px;
  height: 94px;
  animation: ${Animation.cloudTwo} ${timeAnimation}s linear infinite;
`;
export const CloudThree = styled.img`
  position: absolute;
  left: 9%;
  bottom: 12%;
  width: 120px;
  height: 62px;
  animation: ${Animation.cloudThree} ${timeAnimation}s linear infinite;
`;

export const CloudFour = styled.img`
  position: absolute;
  left: 35%;
  bottom: 22%;
  width: 226px;
  height: 137px;
  transform: scale(-1, 1) rotate(360deg);
  animation: ${Animation.cloudFour} ${timeAnimation}s linear infinite;
`;

export const CloudFive = styled.img`
  position: absolute;
  left: 18%;
  bottom: 48%;
  width: 139px;
  height: 72px;
  animation: ${Animation.cloudFive} ${timeAnimation}s linear infinite;
`;

export const CloudSix = styled.img`
  position: absolute;
  right: 0%;
  bottom: 65%;
  width: 209px;
  height: 104px;
  animation: ${Animation.cloudSix} ${timeAnimation}s linear infinite;
`;

export const CloudSeven = styled.img`
  position: absolute;
  top: 17%;
  left: 11%;
  width: 225px;
  height: 116px;
  animation: ${Animation.cloudSeven} ${timeAnimation}s linear infinite;
`;

export const CloudEight = styled.img`
  position: absolute;
  top: 44%;
  right: 28%;
  width: 200px;
  height: 100px;
  animation: ${Animation.cloudEight} ${timeAnimation}s linear infinite;
`;

export const CloudNine = styled.img`
  position: absolute;
  top: 21%;
  right: 42%;
  width: 170px;
  height: 90px;
  animation: ${Animation.cloudNine} ${timeAnimation}s linear infinite;
`;
