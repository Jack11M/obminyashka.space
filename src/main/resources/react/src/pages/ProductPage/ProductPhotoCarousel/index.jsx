import { useEffect, useState } from 'react';

import NoPhoto from './NoPhoto/NoPhoto';
import SliderOrNot from '../SliderOrNot';

import {
  Image,
  CarouselBox,
  SliderPosition,
  ProductPhotoSlideBig,
} from './styles';

const ProductPhotoCarousel = ({ photos = [] }) => {
  const [state, setState] = useState({
    photos: [],
    bigPhoto: {},
  });

  useEffect(() => {
    if (photos.length > 0) {
      setState({
        photos,
        bigPhoto: photos[0],
      });
    }
  }, [photos]);

  const showBigImg = (id) => {
    const currentPhoto = photos.find((photo) => photo.id === id);
    setState({ ...state, bigPhoto: currentPhoto });
  };

  let noArr;
  if (!state.photos.length) {
    noArr = <NoPhoto noPhoto />;
  } else {
    noArr = (
      <Image
        alt="activeSlide"
        src={`data:image/jpeg;base64,${state.bigPhoto.resource}`}
      />
    );
  }

  return (
    <CarouselBox>
      <SliderPosition>
        <SliderOrNot
          photos={state.photos}
          showBigImg={showBigImg}
          bigPhoto={state.bigPhoto}
        />
      </SliderPosition>
      <ProductPhotoSlideBig>{noArr}</ProductPhotoSlideBig>
    </CarouselBox>
  );
};
export default ProductPhotoCarousel;
