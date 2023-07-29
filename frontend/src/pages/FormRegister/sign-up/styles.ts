import { InputField } from "obminyashka-components";
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
  top: -30%;
  right: -50%;
  width: 240px;
  height: 240px;
  animation: ${sunMove} 2.5s infinite;
  transition: all 1s;
  z-index: 1;
`;

export const InputEmail = styled(InputField)``;
export const InputName = styled(InputField)``;
export const InputPass = styled(InputField)``;
export const InputSecPass = styled(InputField)``;

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

    &:has(${InputEmail}:focus) {
      ${SunRegistration} {
        top: -16%;
        right: -37%;
        animation: ${moveSun} 1s forwards, ${sunMove} 2.5s infinite;
      }
    }
    &:has(${InputName}:focus) {
      ${SunRegistration} {
        top: 11%;
        right: -37%;
        animation: ${moveSun} 1s forwards, ${sunMove} 2.5s infinite;
      }
    }
    &:has(${InputPass}:focus) {
      ${SunRegistration} {
        top: 43%;
        right: -37%;
        animation: ${moveSun} 1s forwards, ${sunMove} 2.5s infinite;
      }
    }
    &:has(${InputSecPass}:focus) {
      ${SunRegistration} {
        top: 70%;
        right: -37%;
        animation: ${moveSun} 1s forwards, ${sunMove} 2.5s infinite;
      }
    }
  `}
`;
