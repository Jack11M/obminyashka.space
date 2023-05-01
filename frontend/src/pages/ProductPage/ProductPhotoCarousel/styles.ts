import styled from 'styled-components';

export const CarouselBox = styled.div`
  display: flex;
  flex-wrap: wrap;
  flex-direction: row;
  padding: 0;
  justify-content: space-between;
`;

export const SliderPosition = styled.div`
  width: 160px;
  height: 100%;
  * {
    outline: none;
  }
`;

export const ProductPhotoSlideBig = styled.div`
  width: 600px;
  height: 680px;
  object-fit: contain;
`;

export const Image = styled.img`
  width: 100%;
  height: 100%;
  object-fit: contain;
`;
