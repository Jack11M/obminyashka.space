import styled from 'styled-components';
import { Images } from 'obminyashka-components';

export const PresentSection = styled.div`
  position: relative;
  background-image: url(${Images.headerBg});
  background-size: cover;
  background-position: bottom;
  background-repeat: no-repeat;
  z-index: 0;
`;

export const Wrapper = styled.div`
  width: 100%;
  max-width: 1251px;
  padding: 0 15px;
  margin: 0 auto;
  overflow: hidden;
`;

export const PresentSlider = styled.ul`
  display: flex;
  padding-top: 298px;
  padding-bottom: 201px;
  justify-content: space-between;
  align-items: center;
  font-family: Roboto;
`;

export const PresentSliderElement = styled.li`
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: auto;
  z-index: 10;
`;

export const FirstImage = styled.img`
  margin-bottom: 66px;
`;

export const SecondImage = styled.img`
  margin-bottom: 70px;
`;

export const ThirdImage = styled.img`
  margin-bottom: 37px;
`;

export const TextWrapper = styled.div`
  text-shadow: 1px 1px 10px #12b6ed;
  font-style: normal;
  font-size: 30px;
  line-height: 36px;
  text-align: center;
  color: #ffffff;
`;

export const FirstText = styled.p`
  position: relative;
  max-width: 285px;

  :before {
    position: absolute;
    top: -38px;
    left: -22px;
    content: '';
    width: 41px;
    height: 67px;
    background-image: url(${Images.question});
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
  }
`;

export const SecondText = styled.p`
  position: relative;
  max-width: 386px;
  min-width: 386px;

  :after {
    position: absolute;
    bottom: 7px;
    right: 75px;
    content: '';
    width: 13px;
    height: 60px;
    background-image: url(${Images.answer});
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
  }
`;

export const ThirdText = styled.p`
  max-width: 397px;
`;
