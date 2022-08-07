export const getStorageUser = (option) => {
  const local = JSON.parse(localStorage.getItem(option)) || '';
  const session = JSON.parse(sessionStorage.getItem(option)) || '';
  return local || session || '';
};

export const setStorageUser = (data) => {
  const local = JSON.parse(localStorage.getItem('user'));
  const session = JSON.parse(sessionStorage.getItem('user'));
  if (local) {
    const newData = { ...local, ...data };
    localStorage.setItem('user', JSON.stringify(newData));
  } else {
    const newData = { ...session, ...data };
    sessionStorage.setItem('user', JSON.stringify(newData));
  }
};

export const getStorageLang = () => localStorage.getItem('lang') || 'ua';

export const removeTokenFromStorage = () => {
  localStorage.removeItem('user');
  sessionStorage.removeItem('user');
  return false;
};
