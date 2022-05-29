/* eslint-disable indent */
import { getTranslatedText } from 'components/local/localization';

import { getStorageLang } from '.';

const errorAuth = (errors, key, isValid) => {
  if (isValid) {
    return errors.filter((error) => Object.keys(error).join('') !== key);
  }
  return errors.some((error) => Object.keys(error).join('') === key)
    ? errors
    : [
        ...errors,
        { [key]: getTranslatedText(`errors.${key}`, getStorageLang()) },
      ];
};
const translateErrorsAuth = (state) =>
  state.map((error) => {
    const key = Object.keys(error);
    return {
      [key]: getTranslatedText(`errors.${key}`, getStorageLang()),
    };
  });

const isErrorArray = (event, state) => {
  const { name: key } = event.target;
  return errorAuth(state.errors, key);
};

export { errorAuth, translateErrorsAuth, isErrorArray };
