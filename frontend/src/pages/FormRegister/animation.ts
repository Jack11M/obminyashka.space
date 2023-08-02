import { keyframes } from "styled-components";

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

export const cloudOne = keyframes`
  0% {
    right: 29%;
  }
  70% {
    right: 100%; 
    opacity: 1;
    transform: scale(1);
  }
  70.1% {
    opacity: 0;
    transform: scale(0);
  }
  70.2% {
    right: -10%;
    transform: scale(1);
  }
  71.3% {
    opacity: 1;
  }
  100% {
    right: 29%;
  }
`;

export const cloudTwo = keyframes`
  0% {
    right: 7%;
  }
  85% {
    right: 100%; 
    opacity: 1;
    transform: scale(1);
  }
  85.1% {
    opacity: 0;
    transform: scale(0);
  }
  85.2% {
    right: -8%;
    transform: scale(1);
  }
  86.3% {
    opacity: 1;
  }
  100% {
    right: 7%;
  }
`;

export const cloudThree = keyframes`
  0% {
    left: 9%;
  }
  9% {
    left: -8%; 
    opacity: 1;
    transform: scale(1);
  }
  9.1% {
    opacity: 0;
    transform: scale(0);
  }
  9.2% {
    left: 100%;
    transform: scale(1);
  }
  10.3% {
    opacity: 1;
  }
  100% {
    left: 9%;
  }
`;

export const cloudFour = keyframes`
  0% {
    left: 30%;
  }
  30% {
    left: -10%; 
    opacity: 1;
    visibility: visible;
  }
  30.1% {
    opacity: 0;
    visibility: hidden;
  }
  30.2% {
    left: 100%;
  }
  31.3% {
    opacity: 1;
    visibility: visible;
  }
  100% {
    left: 30%;
  }
`;

export const cloudFive = keyframes`
  0% {
    left: 18%;
  }
  18% {
    left: -10%; 
    opacity: 1;
    transform: scale(1);
  }
  18.1% {
    opacity: 0;
    transform: scale(0);
  }
  18.2% {
    left: 100%;
  }
  19.3% {
    opacity: 1;
    transform: scale(1);
  }
  100% {
    left: 18%;
  }
`;

export const cloudSix = keyframes`
  0% {
    right: 0%;
  }
  96% {
    right: 100%; 
    opacity: 1;
    transform: scale(1);  
  }
  96.1% {
    opacity: 0;
    transform: scale(0);
  }
  96.2% {
    right: -10%;
    transform: scale(1);
  }
  97.3% {
    opacity: 1;
  }
  100% {
    right: 0%;
  }
`;

export const cloudSeven = keyframes`
  0% {
    left: 11%;
  }
  11% {
    left: -10%; 
    opacity: 1;
    transform: scale(1);
  }
  11.1% {
    opacity: 0;
    transform: scale(0);
  }
  11.2% {
    left: 100%;
    transform: scale(1);
  }
  12.3% {
    opacity: 1;
  }
  100% {
    left: 11%;
  } 
`;

export const cloudEight = keyframes`
  0% {
    right: 28%;
  }
  72% {
    right: 100%; 
    opacity: 1;
    visibility: visible;
  }
  72.1% {
    opacity: 0;
    visibility: hidden;
  }
  72.2% {
    right: -10%;
  }
  73.3% {
    opacity: 1;
    visibility: visible;
  }
  100% {
    right: 28%;
  } 
`;

export const cloudNine = keyframes`
  0% {
    right: 42%;
  }
  58% {
    right: 100%; 
    opacity: 1;
    transform: scale(1); 
  }
  58.1% {
    opacity: 0;
    transform: scale(0); 
  }
  58.2% {
    right: -10%;
    transform: scale(1); 
  }
  59.3% {
    opacity: 1;
  }
  100% {
    right: 42%;
  } 
`;
