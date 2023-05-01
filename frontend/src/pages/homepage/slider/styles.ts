import styled from 'styled-components';
import { Link } from 'react-router-dom';

export const CategorySlider = styled.div`
  margin: 125px 0;

  .slick-slide {
    margin-right: 20px;
  }
  .slick-list {
    height: 248px;
  }
`;

export const CategorySliderSpan = styled.span`
  top: 22px;
  right: 22px;
  color: #ffffff;
  font-size: 18px;
  line-height: 24px;
  position: absolute;
  font-family: Roboto;

  :active {
    transform: scale(1.05);
  }
`;

export const CategorySliderImageTitle = styled.b`
  color: #ffffff;
  font-size: 36px;
  line-height: 44px;
  letter-spacing: 1px;
  text-transform: uppercase;
  font-family: Caveat;
`;

export const CategorySliderLink = styled(Link)`
  outline: none;
  position: relative;
`;
