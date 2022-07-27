import styled, { css, keyframes } from 'styled-components';

const animation = keyframes`
  0% {
    top: 24px;
    left: 36px;
    width: 0;
    height: 0;
    opacity: 1;
  }
  100% {
    top: 0;
    left: 0;
    width: 72px;
    height: 49px;
    opacity: 0;
  }
`;

export const styleSet = css`
  position: absolute;
  border: 4px solid #fff;
  opacity: 1;
  border-radius: 50%;
  animation: 1s ${animation} infinite;
`;

export const LdsRipple = styled.div`
  display: inline-block;
  position: relative;
  width: 80px;
  height: 49px;
`;

export const FirstDiv = styled.div`
  ${styleSet};
`;

export const SecondDiv = styled.div`
  ${styleSet};

  animation-delay: -0.5s;
`;
