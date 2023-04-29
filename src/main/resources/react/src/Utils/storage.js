export const getStorageUser = (option) => {
  const local = JSON.parse(localStorage.getItem(option)) || '';
  const session = JSON.parse(sessionStorage.getItem(option)) || '';
  return local || session || '';
};

export const setStorageUser = (data, type = 'user') => {
  const local = JSON.parse(localStorage.getItem(type));
  const session = JSON.parse(sessionStorage.getItem(type));
  let newData;

  if (local && type === 'user') {
    newData = { ...local, ...data };
    localStorage.setItem(type, JSON.stringify(newData));
  }

  if (session && type === 'user') {
    newData = { ...session, ...data };
    sessionStorage.setItem(type, JSON.stringify(newData));
  }
};

export const getDefaultLang = () => {
  const localLang = localStorage.getItem('lang');

  if (localLang) {
    return localLang;
  }

  let lang = navigator.language;
  const getLang = lang?.substring(0, 2);
  lang = getLang === 'en' ? 'en' : 'ua';
  localStorage.setItem('lang', lang);

  return lang;
};

export const getStorageLang = () =>
  getStorageUser('user')?.['Accept-Language'] || getDefaultLang();

export const removeTokenFromStorage = () => {
  localStorage.removeItem('user');
  sessionStorage.removeItem('user');
  return false;
};
