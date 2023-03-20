import {
  toySlider,
  childSlider,
  shoesSlider,
  clothesSlider,
  furnitureSlider,
  strollersSlider,
} from 'assets/img/all_images_export/sliderImages';
import { getTranslatedText } from 'components/local/localization';

const sliderData = [
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

export { sliderData };
