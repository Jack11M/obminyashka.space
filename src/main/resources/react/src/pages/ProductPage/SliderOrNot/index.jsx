// /* eslint-disable jsx-a11y/no-noninteractive-element-interactions */
import Slider from 'react-slick';

import * as Styles from './styles';
import { settings } from './config';
import NoPhoto from '../ProductPhotoCarousel/NoPhoto/NoPhoto';

const SliderOrNot = ({ photos, showBigImg, bigPhoto }) => {
  let variable;

  if (photos.length === 0) {
    variable = <NoPhoto />;
  } else if (photos.length < 5) {
    variable = (
      <>
        {photos.map((photo) => (
          <div key={`index-${photo.id}`}>
            <Styles.Image
              alt={photo.id}
              selected={bigPhoto.id === photo.id}
              onClick={() => showBigImg(photo.id)}
              src={`data:image/jpeg;base64,${photo.resource}`}
            />
          </div>
        ))}
      </>
    );
  } else {
    variable = (
      <Styles.ImageSlider>
        <Slider {...settings}>
          {photos.map((photo) => (
            <div key={`index-${photo.id}`}>
              <Styles.Image
                small
                alt={photo.id}
                selected={bigPhoto.id === photo.id}
                onClick={() => showBigImg(photo.id)}
                src={`data:image/jpeg;base64,${photo.resource}`}
              />
            </div>
          ))}
        </Slider>
      </Styles.ImageSlider>
    );
  }
  return variable;
};
export default SliderOrNot;
