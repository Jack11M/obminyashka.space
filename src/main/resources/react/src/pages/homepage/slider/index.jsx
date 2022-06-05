import { Link } from 'react-router-dom';
import Slider from 'react-slick';

import {
  toySlider,
  childSlider,
  shoesSlider,
  clothesSlider,
  furnitureSlider,
  strollersSlider,
} from 'assets/img/all_images_export/sliderImages';
import { getTranslatedText } from 'components/local/localization';

import './slider.scss';
import 'slick-carousel/slick/slick.scss';
import 'slick-carousel/slick/slick-theme.scss';

const Sliders = () => {
  const isImg = [
    {
      src: toySlider,
      subtitle: getTranslatedText('mainPage.blueSlideSubtitle'),
      title: getTranslatedText('mainPage.blueSlideTitle'),
      href: '#',
      width: 290,
    },
    {
      src: clothesSlider,
      subtitle: getTranslatedText('mainPage.greenSlideSubtitle'),
      title: getTranslatedText('mainPage.greenSlideTitle'),
      href: '#',
      width: 600,
    },
    {
      src: childSlider,
      subtitle: getTranslatedText('mainPage.yellowSlideSubtitle'),
      title: getTranslatedText('mainPage.yellowSlideTitle'),
      href: '#',
      width: 290,
    },
    {
      src: furnitureSlider,
      subtitle: getTranslatedText('mainPage.pinkSlideSubtitle'),
      title: getTranslatedText('mainPage.pinkSlideTitle'),
      href: '#',
      width: 290,
    },
    {
      src: shoesSlider,
      subtitle: getTranslatedText('mainPage.lilacSlideSubtitle'),
      title: getTranslatedText('mainPage.lilacSlideTitle'),
      href: '#',
      width: 600,
    },

    {
      src: strollersSlider,
      subtitle: getTranslatedText('mainPage.orangeSlideSubtitle'),
      title: getTranslatedText('mainPage.orangeSlideTitle'),
      href: '#',
      width: 290,
    },
  ];

  const settings = {
    className: 'slider variable-width',
    dots: false,
    arrows: false,
    infinite: true,
    centerMode: false,
    slidesToShow: 3,
    slidesToScroll: 1,
    autoplay: true,
    speed: 2000,
    autoplaySpeed: 4000,
    cssEase: 'ease-in-out',
    variableWidth: true,
  };

  return (
    <div className="Home-page-slider">
      <Slider {...settings}>
        {isImg.map((image) => (
          <Link
            className="Home-page-slider__link"
            to={image.href}
            style={{ width: image.width }}
            key={image.title}
          >
            <img src={image.src} alt={image.title} />
            <span className="Home-page-slider__link-subTitle">
              {image.subtitle}
              <br />
              <b>{image.title}</b>
            </span>
          </Link>
        ))}
      </Slider>
    </div>
  );
};

export default Sliders;
