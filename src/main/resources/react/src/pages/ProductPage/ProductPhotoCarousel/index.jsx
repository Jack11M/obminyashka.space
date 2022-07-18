import { useEffect, useState } from 'react';

import NoPhoto from './NoPhoto/NoPhoto';
import SliderOrNot from '../SliderOrNot';

import {
  CarouselBox,
  ProductPhotoSlideBig,
  SliderPosition,
  Image,
} from './styles';

const ProductPhotoCarousel = ({ photos }) => {
  const [state, setState] = useState({
    photos: [],
    bigPhoto: {},
  });

  useEffect(() => {
    setState({
      photos,
      bigPhoto: photos[0],
    });
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
        src={`data:image/jpeg;base64,${state.bigPhoto.resource}`}
        alt="activeSlide"
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
