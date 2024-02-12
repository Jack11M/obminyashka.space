import { Images } from "obminyashka-components";

import { getTranslatedText } from "src/components/local";

export const slidesData = [
  {
    href: "#",
    title: getTranslatedText("swiperText.toysTitle"),
    src: Images.toySlider,
    subtitle: getTranslatedText("swiperText.toysSubTitle"),
  },

  {
    href: "#",
    title: getTranslatedText("swiperText.clothesTitle"),
    src: Images.clothesSlider,
    subtitle: getTranslatedText("swiperText.clothesSubTitle"),
  },

  {
    href: "#",
    title: getTranslatedText("swiperText.infantsTitle"),
    src: Images.childSlider,
    subtitle: getTranslatedText("swiperText.infantsSubTitle"),
  },

  {
    href: "#",
    title: getTranslatedText("swiperText.furnitureTitle"),
    src: Images.furnitureSlider,
    subtitle: getTranslatedText("swiperText.furnitureSubTitle"),
  },

  {
    href: "#",
    title: getTranslatedText("swiperText.shoesTitle"),
    src: Images.shoesSlider,
    subtitle: getTranslatedText("swiperText.shoesSubTitle"),
  },

  {
    href: "#",
    title: getTranslatedText("swiperText.transportTitle"),
    src: Images.strollersSlider,
    subtitle: getTranslatedText("swiperText.transportSubTitle"),
  },

  {
    href: "#",
    title: getTranslatedText("swiperText.booksTitle"),
    src: Images.booksSlider,
    subtitle: getTranslatedText("swiperText.booksSubTitle"),
  },
];
