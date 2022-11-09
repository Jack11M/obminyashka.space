export const getStorageUser = (option) => {
  const local = JSON.parse(localStorage.getItem(option)) || '';
  const session = JSON.parse(sessionStorage.getItem(option)) || '';
  return local || session || '';
};

export const setStorageUser = (data, type = 'user') => {
  const local = JSON.parse(localStorage.getItem(type));
  const session = JSON.parse(sessionStorage.getItem(type));
  let newData;
  if (local) {
    newData = { ...local, ...data };
    localStorage.setItem(type, JSON.stringify(newData));
  } else {
    newData = { ...session, ...data };
    sessionStorage.setItem(type, JSON.stringify(newData));
  }
};

export const getDefaultLang = () => {
  let lang = navigator.language || navigator.userLanguage;
  lang = lang?.substring(0, 2);
  switch (lang) {
    case 'ua':
      return 'ua';
    case 'en':
      return 'en';

    default:
      return 'ua';
  }
};

export const getStorageLang = () =>
  getStorageUser('user')?.['Accept-Language'] || getDefaultLang();

export const removeTokenFromStorage = () => {
  localStorage.removeItem('user');
  sessionStorage.removeItem('user');
  return false;
};
