import styled from 'styled-components';
import { Link } from 'react-router-dom';

export const SliderWrapper = styled.div`
  margin: 125px 0;

  .slick-track {
    display: flex;
    flex-direction: row;
  }
  .slick-slide {
    margin-right: 20px;
  }
  .slick-list {
    height: 248px;
  }
`;

export const SliderItem = styled(Link)`
  position: relative;
  outline: none;
`;

export const SliderSubtitle = styled.span`
  position: absolute;
  top: 22px;
  right: 22px;
  display: flex;
  flex-direction: column;
  color: ${({ theme }) => theme.colors.white};
  font-size: 18px;
  line-height: 24px;
  font-family: Roboto;

  :active {
    transform: scale(1.05);
  }
`;

export const SliderTitle = styled.b`
  color: ${({ theme }) => theme.colors.white};
  font-size: 36px;
  line-height: 44px;
  letter-spacing: 1px;
  text-transform: uppercase;
  font-family: Caveat;
`;
