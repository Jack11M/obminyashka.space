import styled, { css, keyframes } from 'styled-components';

const fadeIn = keyframes`
 0% {
    opacity: 0;
  }
  100%{
    opacity: 1;
  }
`;

const moveDown = keyframes`
 0% {
   opacity: 0;

  }
  5%{
    transform: translateY(-100vh);
  }
  100%{
    opacity: 1;
    transform: translateY(0vh);
  }
`;

const fadeOut = keyframes`
 0% {
    opacity: 1;
  }
  100%{
    opacity: 0;
  }
`;

const continueMoveDown = keyframes`
 0% {
   opacity: 1;
   transform: translateY(0vh);
  }
  100%{
    opacity: 0;
     transform: translateY(100vh);
  }
`;

const WrapDiv = styled.div<{ closing: boolean }>`
  position: fixed;
  display: flex;
  justify-content: center;
  align-items: center;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(17, 181, 237, 0.4);
  z-index: 999;
  ${(p) => css`
    animation: ${p.closing ? fadeOut : fadeIn} 0.3s cubic-bezier(0.44, 0.44, 0.61, 0.63);
  `}
`;
const ContentModal = styled.div<{ closing: boolean }>`
  position: relative;
  max-width: 600px;
  padding: 44px 50px;
  background-color: #fff;
  box-shadow: 0 0 75px 10px rgba(17, 181, 237, 0.6);
  opacity: 1;
  ${(p) => css`
    animation: ${p.closing ? continueMoveDown : moveDown} 0.3s cubic-bezier(0.44, 0.44, 0.61, 0.63);
  `}
`;

const Title = styled.div`
  margin-bottom: 25px;
  line-height: 16px;
  font-size: 20px;
  font-style: normal;
  font-weight: 600;
  color: #111111;
`;

const Content = styled.div`
  margin: 0 auto;
  font-size: 16px;
  font-style: normal;
  font-weight: 600;
  line-height: 22px;
  color: #777777;
`;

const DivClose = styled.div`
  position: absolute;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 30px;
  height: 30px;
  top: -15px;
  right: -15px;
  border-radius: 50%;
  background-color: #12b6ed;
  cursor: pointer;
  transition: 0.3s ease-in-out;
  transition-delay: 0.1s;

  &:before,
  &:after {
    position: absolute;
    content: '';
    width: 2px;
    height: 15px;
    transform: rotate(45deg);
    background-color: #fff;
  }
  &:before {
    transform: rotate(45deg);
  }
  &:after {
    transform: rotate(-45deg);
  }
  &:hover {
    transform: rotate(180deg);
  }
`;

export { WrapDiv, ContentModal, DivClose, Title, Content };
