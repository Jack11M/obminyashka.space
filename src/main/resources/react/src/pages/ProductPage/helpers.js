export const getDate = (lang) => {
  const date = new Date();
  const day = date.getDate();
  const month = date.getMonth() + 1;
  const year = date.getFullYear();

  return lang === 'en' ? `${month}.${day}.${year}` : `${day}.${month}.${year}`;
};
