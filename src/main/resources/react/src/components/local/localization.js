import { store } from 'store';

import ru from './ru';
import ua from './ua';
import en from './en';

const translations = { ru, ua, en };

const getTranslatedText = (key) => {
  const { lang } = store.getState().auth;
  const arrayKeys = key.split('.');
  const [keys, value] = arrayKeys;
  const currentTranslation = translations[lang];

  return currentTranslation[keys][value];
};

export { getTranslatedText };
