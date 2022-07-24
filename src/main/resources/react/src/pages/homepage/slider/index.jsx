import Slider from 'react-slick';
import { useSelector } from 'react-redux';

import { getLang } from 'store/auth/slice';
import {
  toySlider,
  childSlider,
  shoesSlider,
  clothesSlider,
  furnitureSlider,
  strollersSlider,
} from 'assets/img/all_images_export/sliderImages';
import { getTranslatedText } from 'components/local/localization';

import * as Styles from './styles';
import { settings } from './config';
import 'slick-carousel/slick/slick.scss';
import 'slick-carousel/slick/slick-theme.scss';

const Sliders = () => {
  const lang = useSelector(getLang);

  const isImg = [
    {
      href: '#',
      width: 290,
      src: toySlider,
      title: getTranslatedText('mainPage.blueSlideTitle', lang),
      subtitle: getTranslatedText('mainPage.blueSlideSubtitle', lang),
    },

    {
      href: '#',
      width: 600,
      src: clothesSlider,
      title: getTranslatedText('mainPage.greenSlideTitle', lang),
      subtitle: getTranslatedText('mainPage.greenSlideSubtitle', lang),
    },

    {
      href: '#',
      width: 290,
      src: childSlider,
      title: getTranslatedText('mainPage.yellowSlideTitle', lang),
      subtitle: getTranslatedText('mainPage.yellowSlideSubtitle', lang),
    },

    {
      href: '#',
      width: 290,
      src: furnitureSlider,
      title: getTranslatedText('mainPage.pinkSlideTitle', lang),
      subtitle: getTranslatedText('mainPage.pinkSlideSubtitle', lang),
    },

    {
      href: '#',
      width: 600,
      src: shoesSlider,
      title: getTranslatedText('mainPage.lilacSlideTitle', lang),
      subtitle: getTranslatedText('mainPage.lilacSlideSubtitle', lang),
    },

    {
      href: '#',
      width: 290,
      src: strollersSlider,
      title: getTranslatedText('mainPage.orangeSlideTitle', lang),
      subtitle: getTranslatedText('mainPage.orangeSlideSubtitle', lang),
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

              <Styles.CategorySliderImageTitle>
                {image.title}
              </Styles.CategorySliderImageTitle>
            </Styles.CategorySliderSpan>
          </Styles.CategorySliderLink>
        ))}
      </Slider>
    </Styles.CategorySlider>
  );
};

export default Sliders;
