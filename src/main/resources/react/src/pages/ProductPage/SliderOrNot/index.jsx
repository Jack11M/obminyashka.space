import Slider from 'react-slick';

import * as Styles from './styles';
import { settings } from './config';
import { NoPhoto } from '../ProductPhotoCarousel/NoPhoto';

const SliderOrNot = ({ photos = [], showBigImg, bigPhoto }) => {
  let variable;

  if (photos?.length === 0) {
    variable = <NoPhoto />;
  } else if (photos.length < 5) {
    variable = (
      <>
        {photos.map((photo) => (
          <div key={`index-${photo.id}`}>
            <Styles.Image
              alt={photo.id}
              src={photo.resource}
              selected={bigPhoto.id === photo.id}
              onClick={() => showBigImg(photo.id)}
            />
          </div>
        ))}
      </>
    );
  } else {
    variable = (
      <Styles.ImageSlider>
        <Slider {...settings}>
          {photos?.map((photo) => (
            <div key={`index-${photo.id}`}>
              <Styles.Image
                small
                alt={photo.id}
                src={photo.resource}
                selected={bigPhoto.id === photo.id}
                onClick={() => showBigImg(photo.id)}
              />
            </div>
          ))}
        </Slider>
      </Styles.ImageSlider>
    );
  }

  return variable;
};

export { SliderOrNot };
