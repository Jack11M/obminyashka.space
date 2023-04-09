import styled from 'styled-components';

export const ModalTitle = styled.p`
  /* position: absolute; */
  /* top: 44px;
  left: 50px; */
  display: block;
  font-size: 20px;
  font-weight: 600;
  line-height: 16px;
  font-family: Proxima Nova, sans-serif;
  padding: 10px 0 5px;
  color: #111111;
`;

export const ModalText = styled.p`
  display: flex;
  align-items: center;
  /* top: 75px;
  left: 53px; */
  width: 310px;
  height: 63px;
  color: #777777;
  font-size: 16px;
  text-align: left;
  line-height: 22px;
  margin: 15px 0 10px;
  /* position: absolute; */
`;

export const ButtonStyles = styled.div`
  /* top: 148px;
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
  transition: all 0.3s linear; */
`;

export const ModalBackground = styled.div`
  position: absolute;
  right: 33px;
  top: 39px;
  width: 146px;
  height: 146px;
  background-color: #fee6e6;
  border-radius: 25px;
`;

export const ModalImage = styled.img`
  position: absolute;
  top: 32%;
  left: 77%;
  transform: translate(calc(50% - 107px), calc(50% - 39px));
`;
