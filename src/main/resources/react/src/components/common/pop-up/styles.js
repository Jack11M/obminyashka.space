import styled, { css } from 'styled-components';

const WrapDiv = styled.div`
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
  transition: 0.4s ease-in-out;

  opacity: ${(p) => (p.active ? 1 : 0)};
`;
const ContentModal = styled.div`
  position: relative;
  max-width: 600px;
  flex-grow: 1;
  padding: 44px 50px;
  background-color: #fff;
  transition: 0.4s ease-in-out 0.1s;
  ${(p) => css`
    transform: translateY(${p.active ? 0 : -100}vh);
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
    transform: rotate(-180deg);
  }
`;

export { WrapDiv, ContentModal, DivClose, Title };
