import Slider from 'react-slick';

import * as Styles from './styles';
import { sliderData } from './mock';
import { settings } from './config';

const Slides = () => {
  return (
    <Styles.CategorySliderWrapper>
      <Slider {...settings}>
        {sliderData.map((image) => (
          <Styles.CategorySliderItem
            to={image.href}
            key={image.title}
            style={{ width: image.width }}
          >
            <img src={image.src} alt={image.title} />

            <Styles.SubtitleSpan>
              {image.subtitle}

              <Styles.Title>{image.title}</Styles.Title>
            </Styles.SubtitleSpan>
          </Styles.CategorySliderItem>
        ))}
      </Slider>
    </Styles.CategorySliderWrapper>
  );
};

export default Slides;
