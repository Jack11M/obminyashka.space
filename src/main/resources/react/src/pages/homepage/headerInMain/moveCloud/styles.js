import styled, { keyframes } from 'styled-components';

import firstCloud from 'assets/img/clouds/Cloud1.png';
import secondCloud from 'assets/img/clouds/Cloud2.png';
import thirdCloud from 'assets/img/clouds/Cloud3.png';

const move1 = keyframes`
  0% {
    left: 3vw;
  }
  87.3% {
    opacity: 1;
    left: 100vw;
  }
  87.4% {
    opacity: 0;
    left: 100vw;
  }
  87.5% {
    opacity: 0;
    left: -10vw;
  }
  87.6% {
    opacity: 1;
  }
  100% {
    left: 3vw;
  }
`;

const move2 = keyframes`
  0% {
    right: 2vw;
  }
  88.2% {
    opacity: 0.6;
    right: 100vw;
  }
  88.3% {
    opacity: 0;
    right: 100vw;
  }
  88.4% {
    opacity: 0;
    right: -10vw;
  }
  88.5% {
    opacity: 0.6;
  }
  100% {
    right: 2vw;
  }
`;

const move3 = keyframes`
  0% {
    right: 12vw;
  }
  79.2% {
    opacity: 1;
    right: 100vw;
  }
  79.3% {
    opacity: 0;
    right: 100vw;
  }
  79.4% {
    opacity: 0;
    right: -10vw;
  }
  79.5% {
    opacity: 1;
  }
  100% {
    right: 12vw;
  }
`;

const move4 = keyframes`
  0% {
    left: 42vw;
  }
  52.2% {
    opacity: 0.7;
    left: 100vw;
  }
  52.3% {
    opacity: 0;
    left: 100vw;
  }
  52.4% {
    opacity: 0;
    left: -10vw;
  }
  52.5% {
    opacity: 0.7;
  }
  100% {
    left: 42vw;
  }
`;

const move5 = keyframes`
  0% {
    left: 11vw;
  }
  86.3% {
    opacity: 1;
    left: 100vw;
  }
  86.4% {
    opacity: 0;
    left: 100vw;
  }
  86.5% {
    opacity: 0;
    left: -4vw;
  }
  86.6% {
    opacity: 1;
  }
  100% {
    left: 11vw;
  }
`;

const move6 = keyframes`
  0% {
    right: 26vw;
  }
  71.78% {
    opacity: 1;
    right: 100vw;
  }
  71.79% {
    opacity: 0;
    right: 100vw;
  }
  71.8% {
    opacity: 0;
    right: -3vw;
  }
  71.82% {
    opacity: 1;
  }
  100% {
    right: 26vw;
  }
`;

export const CloudsWrapper = styled.div`
  overflow: hidden;
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  opacity: 1;
  z-index: 5;
`;

export const FirstCloud = styled.span`
  position: absolute;
  top: 190px;
  left: 3vh;
  width: 184px;
  height: 131px;
  background-image: url(${firstCloud});
  background-position: center center;
  background-repeat: no-repeat;

  animation-name: ${move1};
  animation-duration: 100s;
  animation-timing-function: linear;
  animation-iteration-count: infinite;
  animation-direction: reverse;
`;

export const SecondCloud = styled.span`
  position: absolute;
  bottom: 200px;
  right: 2vw;
  width: 184px;
  height: 131px;
  background-image: url(${firstCloud});
  background-position: center center;
  background-repeat: no-repeat;

  animation-name: ${move2};
  animation-duration: 180s;
  animation-timing-function: linear;
  animation-iteration-count: infinite;
  opacity: 0.6;
`;

export const ThirdCloud = styled.span`
  position: absolute;
  top: 325px;
  right: 12vw;
  width: 190px;
  height: 119px;
  background-image: url(${secondCloud});
  background-position: center center;
  background-repeat: no-repeat;

  animation-name: ${move3};
  animation-duration: 150s;
  animation-timing-function: linear;
  animation-iteration-count: infinite;
  opacity: 1;
`;

export const FourthCloud = styled.span`
  position: absolute;
  bottom: 300px;
  left: 42vw;
  width: 190px;
  height: 119px;
  background-image: url(${secondCloud});
  background-position: center center;
  background-repeat: no-repeat;

  animation-name: ${move4};
  animation-duration: 130s;
  animation-timing-function: linear;
  animation-iteration-count: infinite;
  animation-direction: reverse;
  opacity: 0.7;
`;

export const FifthCloud = styled.span`
  position: absolute;
  bottom: 370px;
  left: 11vw;
  width: 106px;
  height: 67px;
  background-image: url(${thirdCloud});
  background-position: center center;
  background-repeat: no-repeat;

  animation-name: ${move5};
  animation-duration: 55s;
  animation-timing-function: linear;
  animation-iteration-count: infinite;
  animation-direction: reverse;
`;

export const SixthCloud = styled.span`
  position: absolute;
  top: 370px;
  right: 26vw;
  width: 106px;
  height: 67px;
  background-image: url(${thirdCloud});
  background-position: center center;
  background-repeat: no-repeat;

  animation-name: ${move6};
  animation-duration: 90s;
  animation-timing-function: linear;
  animation-iteration-count: infinite;
`;
