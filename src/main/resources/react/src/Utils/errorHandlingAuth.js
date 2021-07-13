import { getStorageLang } from '.';
import { getTranslatedText } from 'components/local/localisation';

const errorAuth = (errors, key, isValid) => {
  if (isValid) {
    return errors.filter((error) => Object.keys(error).join('') !== key);
  } else {
    return errors.some((error) => Object.keys(error).join('') === key)
      ? errors
      : [
          ...errors,
          { [key]: getTranslatedText(`errors.${key}`, getStorageLang()) },
        ];
  }
};

const translateErrorsAuth = (state) => {
  return state.map((error) => {
    const key = Object.keys(error);
    return {
      [key]: getTranslatedText(`errors.${key}`, getStorageLang()),
    };
  });
};

const isErrorArray = (event, state) => {
  const { name: key } = event.target;
  return errorAuth(state.errors, key);
};

export { errorAuth, translateErrorsAuth, isErrorArray };
