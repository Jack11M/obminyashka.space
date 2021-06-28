import { getStorageLang, validation } from './index';
import { getTranslatedText } from '../components/local/localisation';

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
  const { name: key, value } = event.target;
  const isValid = validation(state, key, value);
  return errorAuth(state.errors, key, isValid);
};

export { errorAuth, translateErrorsAuth, isErrorArray };
