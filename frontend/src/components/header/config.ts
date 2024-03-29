import { route } from "src/routes/routeConstants";

import { getTranslatedText } from "../local";

export const categories = [
  {
    link: route.home,
    text: getTranslatedText("header.clothes"),
  },
  {
    link: route.home,
    text: getTranslatedText("header.shoes"),
  },
  {
    link: route.home,
    text: getTranslatedText("header.toys"),
  },
  {
    link: route.home,
    text: getTranslatedText("header.vehicles"),
  },
  {
    link: route.home,
    text: getTranslatedText("header.furniture"),
  },
  {
    link: route.home,
    text: getTranslatedText("header.babies"),
  },
  {
    link: route.home,
    text: getTranslatedText("header.books"),
  },
  {
    link: route.home,
    text: getTranslatedText("header.another"),
  },
];

export const burgerLinks = [
  { to: route.userInfo, text: getTranslatedText("header.account") },
  { to: route.addAdv, text: getTranslatedText("button.addAdv"), mobile: true },
  { to: route.home, text: getTranslatedText("header.goodness"), icon: true },
  { to: route.home, text: getTranslatedText("header.about") },
];
