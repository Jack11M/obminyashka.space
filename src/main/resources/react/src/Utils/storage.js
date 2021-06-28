export const getStorageUser = (option) => {
  const local = JSON.parse(localStorage.getItem(option)) || '';
  const session = JSON.parse(sessionStorage.getItem(option)) || '';
  return local || session || '';
};

export const getStorageLang = () => {
  return localStorage.getItem('lang') || 'ru';
};

export const removeTokenFromStorage = () => {
  localStorage.removeItem('user');
  sessionStorage.removeItem('user');
  return false;
};
