import { route } from "src/routes/routeConstants";

import { getTranslatedText } from "../local";

export const categories = [
  {
    link: route.SearchResults + "?categoryId=1",
    text: getTranslatedText("header.clothes"),
  },
  {
    link: route.SearchResults + "?categoryId=2",
    text: getTranslatedText("header.shoes"),
  },
  {
    link: route.SearchResults + "?categoryId=3",
    text: getTranslatedText("header.toys"),
  },
  {
    link: route.SearchResults + "?categoryId=4",
    text: getTranslatedText("header.vehicles"),
  },
  {
    link: route.SearchResults + "?categoryId=5",
    text: getTranslatedText("header.furniture"),
  },
  {
    link: route.SearchResults + "?categoryId=6",
    text: getTranslatedText("header.babies"),
  },
  {
    link: route.SearchResults + "?categoryId=7",
    text: getTranslatedText("header.books"),
  },
  {
    link: route.SearchResults + "?categoryId=8",
    text: getTranslatedText("header.another"),
  },
];

export const burgerLinks = [
  { to: route.userInfo, text: getTranslatedText("header.account") },
  { to: route.addAdv, text: getTranslatedText("button.addAdv"), mobile: true },
  { to: route.home, text: getTranslatedText("header.goodness"), icon: true },
  { to: route.home, text: getTranslatedText("header.about") },
];
