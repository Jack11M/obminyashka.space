import { getTranslatedText } from 'components/local/localization';

const errorAuth = (errors, key, isValid) => {
  if (isValid) {
    return errors.filter((error) => Object.keys(error).join('') !== key);
  }
  return errors.some((error) => Object.keys(error).join('') === key)
    ? errors
    : [...errors, { [key]: getTranslatedText(`errors.${key}`) }];
};
const translateErrorsAuth = (state) =>
  state.map((error) => {
    const key = Object.keys(error);
    return {
      [key]: getTranslatedText(`errors.${key}`),
    };
  });

const isErrorArray = (event, state) => {
  const { name: key } = event.target;
  return errorAuth(state.errors, key);
};

export { errorAuth, translateErrorsAuth, isErrorArray };
