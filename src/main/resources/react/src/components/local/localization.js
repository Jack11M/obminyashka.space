import ua from './ua';
import en from './en';

const translations = { ua, en };

const getTranslatedText = (key, lang) => {
  const arrayKeys = key.split('.');
  const [keys, value] = arrayKeys;

  const currentTranslation = translations[lang];
  return currentTranslation[keys][value];
};

export { getTranslatedText };
