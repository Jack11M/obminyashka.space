import styled from 'styled-components';

export const ModalOverlay = styled.div`
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  opacity: 1;
  width: 100%;
  height: 100%;
  position: fixed;
  background-color: rgba(18, 182, 237, 0.3);
  z-index: 99999;
`;

export const Modal = styled.div`
  top: 50%;
  left: 50%;
  width: 100%;
  height: 240px;
  padding: 39px;
  position: fixed;
  max-width: 599px;
  text-align: center;
  border-radius: 3px;
  transition: 0.3s all;
  box-sizing: border-box;
  background-color: #fff;
  transform: translate(-50%, -50%);
  box-shadow: 0 3px 10px -0.5px rgba(18, 182, 237, 0.3);
  z-index: 30;
`;

export const ModalCross = styled.span`
  top: -15px;
  fill: #444;
  width: 30px;
  height: 30px;
  right: -15px;
  cursor: pointer;
  position: absolute;
  border-radius: 50%;
  background-color: #12b6ed;
  transition: all 0.3s linear;

  :before,
  :after {
    top: 7px;
    width: 1px;
    content: '';
    height: 15px;
    position: absolute;
    background-color: #fff;
  }

  :before {
    transform: rotate(45deg);
    -webkit-transform: rotate(45deg);
  }
  :after {
    transform: rotate(-45deg);
    -webkit-transform: rotate(-45deg);
  }
  :hover {
    background-color: #0094ff;
    transform: rotate(90deg);
    -webkit-transform: rotate(-90deg);
  }
`;

export const ModalTitle = styled.p`
  top: 44px;
  left: 50px;
  display: block;
  color: #111111;
  font-size: 20px;
  font-weight: 600;
  line-height: 16px;
  position: absolute;
  font-family: Proxima Nova, sans-serif;
`;

export const ModalText = styled.p`
  top: 75px;
  left: 53px;
  width: 310px;
  height: 63px;
  display: flex;
  color: #777777;
  font-size: 16px;
  text-align: left;
  line-height: 22px;
  position: absolute;
  align-items: center;
`;

export const ButtonStyles = styled.div`
  top: 148px;
  left: 53px;
  width: 179px;
  height: 49px;
  display: flex;
  color: #ffffff;
  font-size: 16px;
  line-height: 19px;
  position: absolute;
  align-items: center;
  border-radius: 63px;
  text-decoration: none;
  justify-content: center;
  background-color: #12b6ed;
  text-transform: uppercase;
  transition: all 0.3s linear;
`;

export const ModalBackground = styled.div`
  right: 33px;
  width: 146px;
  height: 146px;
  position: absolute;
  border-radius: 25px;
  background-color: #fee6e6;
`;
export const ModalImage = styled.img`
  top: 32%;
  left: 77%;
  position: absolute;
  transform: translate(calc(50% - 107px), calc(50% - 39px));
`;
