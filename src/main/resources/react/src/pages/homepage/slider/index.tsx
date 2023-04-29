import Slider from 'react-slick';

import {
  toySlider,
  childSlider,
  shoesSlider,
  clothesSlider,
  furnitureSlider,
  strollersSlider,
} from 'src/assets/img/all_images_export';
import { getTranslatedText } from 'src/components/local/localization';

import * as Styles from './styles';
import { settings } from './config';
import 'slick-carousel/slick/slick.scss';
import 'slick-carousel/slick/slick-theme.scss';

const Sliders = () => {
  const isImg = [
    {
      href: '#',
      width: 290,
      src: toySlider,
      title: getTranslatedText('mainPage.blueSlideTitle'),
      subtitle: getTranslatedText('mainPage.blueSlideSubtitle'),
    },

    {
      href: '#',
      width: 600,
      src: clothesSlider,
      title: getTranslatedText('mainPage.greenSlideTitle'),
      subtitle: getTranslatedText('mainPage.greenSlideSubtitle'),
    },

    {
      href: '#',
      width: 290,
      src: childSlider,
      title: getTranslatedText('mainPage.yellowSlideTitle'),
      subtitle: getTranslatedText('mainPage.yellowSlideSubtitle'),
    },

    {
      href: '#',
      width: 290,
      src: furnitureSlider,
      title: getTranslatedText('mainPage.pinkSlideTitle'),
      subtitle: getTranslatedText('mainPage.pinkSlideSubtitle'),
    },

    {
      href: '#',
      width: 600,
      src: shoesSlider,
      title: getTranslatedText('mainPage.lilacSlideTitle'),
      subtitle: getTranslatedText('mainPage.lilacSlideSubtitle'),
    },

    {
      href: '#',
      width: 290,
      src: strollersSlider,
      title: getTranslatedText('mainPage.orangeSlideTitle'),
      subtitle: getTranslatedText('mainPage.orangeSlideSubtitle'),
    },
  ];

  return (
    <Styles.CategorySlider>
      <Slider {...settings}>
        {isImg.map((image) => (
          <Styles.CategorySliderLink
            to={image.href}
            key={image.title}
            style={{ width: image.width }}
          >
            <img src={image.src} alt={image.title} />

            <Styles.CategorySliderSpan>
              {image.subtitle}

              <br />

              <Styles.CategorySliderImageTitle>{image.title}</Styles.CategorySliderImageTitle>
            </Styles.CategorySliderSpan>
          </Styles.CategorySliderLink>
        ))}
      </Slider>
    </Styles.CategorySlider>
  );
};

export default Sliders;
