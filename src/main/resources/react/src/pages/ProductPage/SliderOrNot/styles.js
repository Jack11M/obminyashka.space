import styled, { css } from 'styled-components';

import arrowUp from 'assets/img/showAdv/ProductCarouselArrowUp.svg';
import arrowDown from 'assets/img/showAdv/ProductCarouselArrowDown.svg';

export const Image = styled.img`
  height: 140px;
  width: 140px;
  margin: 10px 0;
  object-fit: contain;

  ${({ selected, small }) => css`
    ${small &&
    css`
      height: 138px;
      width: 138px;
      margin: 0;
    `};

    border: ${selected ? '2px solid #fa9e25' : 'none'};

    :hover {
      box-shadow: 0 0 3px 2px rgba(48, 50, 50, 0.4);
      transform: scale(1.1);
    }
  `}
`;

export const ImageSlider = styled.div`
  .slick-next {
    top: 4px;
    right: 70px;
  }

  .slick-prev {
    left: 70px;
    top: 670px;
  }

  .slick-next:before {
    content: url(${arrowUp});
  }

  .slick-prev:before {
    content: url(${arrowDown});
  }

  .slick-list {
    padding-left: 11px;
  }

  .slick-slide {
    margin: 0;
    transition: 0.5s;
    padding: 9px 0;
  }

  .slick-slider {
    padding-top: 20px;
  }
`;
