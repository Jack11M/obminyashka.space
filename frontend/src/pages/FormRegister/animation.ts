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
    opacity: 1;
    display: block;  
  }
  70.1% {
    display: none;  
    right: 100%; 
    opacity: 0;
  }
  70.2% {
    display: block;  
    right: -10%;
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
    display: block; 
    opacity: 1;
  }
  85.1% {
    display: none; 
    right: 100%; 
    opacity: 0;
  }
  85.2% {
    display: block; 
    right: -8%;
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
    display: block; 
    opacity: 1;
  }
  9.1% {
    display: none; 
    left: -8%; 
    opacity: 0;
  }
  9.2% {
    display: block; 
    left: 100%;
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
    opacity: 1;
    display: block;  
  }
  30.1% {
    left: -10%; 
    opacity: 0;
    display: none;  
  }
  30.2% {
    display: block;  
    left: 100%;
  }
  31.3% {
    opacity: 1;
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
    display: block;
    opacity: 1;
  }
  18.1% {
    display: none;
    left: -10%; 
    opacity: 0;
  }
  18.2% {
    display: block;
    left: 100%;
  }
  19.3% {
    opacity: 1;
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
    display: block;
    opacity: 1;
  }
  96.1% {
    display: none;
    right: 100%; 
    opacity: 0;
  }
  96.2% {
    display: block;
    right: -10%;
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
    display: block;
    opacity: 1;
  }
  11.1% {
    display: none;
    left: -10%; 
    opacity: 0;
  }
  11.2% {
    display: block;
    left: 100%;
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
    display: block;
    opacity: 1;
  }
  72.1% {
    display: none;
    right: 100%; 
    opacity: 0;
  }
  72.2% {
    display: block;
    right: -10%;
  }
  73.3% {
    opacity: 1;
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
    display: block;
    opacity: 1;
  }
  58.1% {
    display: none;
    right: 100%; 
    opacity: 0;
  }
  58.2% {
    display: block;
    right: -10%;
  }
  59.3% {
    opacity: 1;
  }
  100% {
    right: 42%;
  } 
`;
