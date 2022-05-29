import { useEffect, useState } from 'react';

import SliderOrNot from '../SliderOrNot';
import NoPhoto from './NoPhoto';

import './ProductPhotoCarousel.scss';

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
    noArr = <NoPhoto noPhoto="bigNoPhoto" noPhotoImg="bigNoPhotoImg" />;
  } else {
    noArr = (
      <img
        src={`data:image/jpeg;base64,${state.bigPhoto.resource}`}
        alt="activeSlide"
      />
    );
  }

  return (
    <div className="carouselBox">
      <div className="sliderPosition">
        <SliderOrNot
          photos={state.photos}
          showBigImg={showBigImg}
          bigPhoto={state.bigPhoto}
        />
      </div>
      <div className="productPhotoSlideBig">{noArr}</div>
    </div>
  );
};
export default ProductPhotoCarousel;
