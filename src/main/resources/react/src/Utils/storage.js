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

export const getStorageLang = () =>
  getStorageUser('user')?.['Accept-Language'] || 'ua';

export const removeTokenFromStorage = () => {
  localStorage.removeItem('user');
  sessionStorage.removeItem('user');
  return false;
};
