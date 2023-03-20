import Slider from 'react-slick';

import * as Styles from './styles';
import { sliderData } from './mock';
import { settings } from './config';

const Slides = () => {
  return (
    <Styles.SliderWrapper>
      <Slider {...settings}>
        {sliderData.map((image) => (
          <Styles.SliderItem
            to={image.href}
            key={image.title}
            style={{ width: image.width }}
          >
            <img src={image.src} alt={image.title} />

            <Styles.SliderSubtitle>
              {image.subtitle}

              <Styles.SliderTitle>{image.title}</Styles.SliderTitle>
            </Styles.SliderSubtitle>
          </Styles.SliderItem>
        ))}
      </Slider>
    </Styles.SliderWrapper>
  );
};

export default Slides;
