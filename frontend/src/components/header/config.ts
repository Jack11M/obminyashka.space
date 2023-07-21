import { getTranslatedText } from '../local';
import { route } from 'src/routes/routeConstants';

export const categories = [
  {
    link: route.home,
    text: getTranslatedText('header.clothes'),
  },
  {
    link: route.home,
    text: getTranslatedText('header.shoes'),
  },
  {
    link: route.home,
    text: getTranslatedText('header.toys'),
  },
  {
    link: route.home,
    text: getTranslatedText('header.vehicles'),
  },
  {
    link: route.home,
    text: getTranslatedText('header.furniture'),
  },
  {
    link: route.home,
    text: getTranslatedText('header.babies'),
  },
  {
    link: route.home,
    text: getTranslatedText('header.books'),
  },
  {
    link: route.home,
    text: getTranslatedText('header.another'),
  },
];

export const burgerLinks = [
  { to: '/', text: getTranslatedText('header.account') },
  { to: '/', text: getTranslatedText('button.addAdv'), mobile: true },
  { to: '/', text: getTranslatedText('header.goodness'), icon: true },
  { to: '/', text: getTranslatedText('header.about') },
];
