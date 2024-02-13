import styled, { css } from "styled-components";

export const SunRegistration = styled.img`
  position: absolute;
  top: -18%;
  right: -37%;
  width: 240px;
  height: 240px;
  transform: rotate(-28deg);
  transition: all 1s;
`;

export const Wrapper = styled.div`
  position: relative;

  &:has(input[name="email"]:focus) {
    ${SunRegistration} {
      top: -1%;
      right: -23%;
      transform: rotate(-1deg);
    }
  }
  &:has(input[name="username"]:focus) {
    ${SunRegistration} {
      top: 15%;
      right: -23%;
      transform: rotate(-9deg);
    }
  }
  &:has(input[name="password"]:focus) {
    ${SunRegistration} {
      top: 25%;
      right: -23%;
      transform: rotate(-22deg);
    }
  }
  &:has(input[name="confirmPassword"]:focus) {
    ${SunRegistration} {
      top: 35%;
      right: -23%;
      transform: rotate(-35deg);
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
