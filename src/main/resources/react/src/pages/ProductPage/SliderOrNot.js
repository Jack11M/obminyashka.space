import Slider from 'react-slick';

import NoPhoto from './ProductPhotoCarousel/NoPhoto';

const settings = {
  dots: false,
  infinite: true,
  slidesToShow: 4,
  slidesToScroll: 1,
  centerMode: false,
  vertical: true,
  verticalSwiping: true,
};

const SliderOrNot = ({ photos, showBigImg, bigPhoto }) => {
  let variable;

  if (!photos.length) {
    variable = <NoPhoto />;
  } else if (photos.length < 5) {
    variable = (
      <>
        {photos.map((photo) => (
          <div key={`index-${photo.id}`}>
            <img
              alt={photo.id}
              src={`data:image/jpeg;base64,${photo.resource}`}
              onClick={() => showBigImg(photo.id)}
              className={`noCarouselImg ${
                bigPhoto.id === photo.id ? 'selected' : ''
              }`}
            />
          </div>
        ))}
      </>
    );
  } else {
    variable = (
      <Slider {...settings} className="productPhotoSlider">
        {photos.map((photo) => (
          <div key={`index-${photo.id}`}>
            <img
              alt={photo.id}
              onClick={() => showBigImg(photo.id)}
              src={`data:image/jpeg;base64,${photo.resource}`}
              className={bigPhoto.id === photo.id ? 'selected' : ''}
            />
          </div>
        ))}
      </Slider>
    );
  }
  return variable;
};
export default SliderOrNot;
