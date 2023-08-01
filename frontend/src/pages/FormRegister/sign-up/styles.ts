import styled, { css, keyframes } from "styled-components";

import { sunMove } from "../styles";

const moveSun = keyframes`
  0% {
    transform: translate(0);
  }
  100% {
    transform: translate(100px);
  }
`;

export const SunRegistration = styled.img`
  position: absolute;
  top: -18%;
  right: -37%;
  width: 240px;
  height: 240px;
  animation: ${sunMove} 2.5s infinite;
  transition: all 1s;
`;

export const Wrapper = styled.div`
  position: relative;

  &:has(input[name="email"]:focus) {
    ${SunRegistration} {
      top: -3%;
      right: -24%;
      animation: ${moveSun} 1s forwards, ${sunMove} 2.5s infinite;
    }
  }
  &:has(input[name="username"]:focus) {
    ${SunRegistration} {
      top: 11%;
      right: -34%;
      animation: ${moveSun} 1s forwards, ${sunMove} 2.5s infinite;
    }
  }
  &:has(input[name="password"]:focus) {
    ${SunRegistration} {
      top: 43%;
      right: -34%;
      animation: ${moveSun} 1s forwards, ${sunMove} 2.5s infinite;
    }
  }
  &:has(input[name="confirmPassword"]:focus) {
    ${SunRegistration} {
      top: 70%;
      right: -34%;
      animation: ${moveSun} 1s forwards, ${sunMove} 2.5s infinite;
    }
  }
`;

export const FormikContainer = styled.div`
  position: relative;
  padding: 0 15px;
  background-color: white;
  z-index: 1;

  ${({ theme }) => css`
    ${theme.responsive.isTablet &&
    css`
      padding: 0 45px;
    `}
  `}
`;

export const WrapperInputSingUp = styled.div`
  position: relative;
  display: flex;
  flex-direction: column;
  gap: 26px;
  padding-top: 3px;
  margin-bottom: 30px;

  ${({ theme }) => css`
    ${theme.responsive.isTablet &&
    css`
      gap: 42px;
    `}
  `}
`;
