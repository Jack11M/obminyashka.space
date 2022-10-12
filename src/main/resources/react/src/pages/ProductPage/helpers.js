import { city, district } from 'Utils';

export const getDate = (lang) => {
  const date = new Date();
  const day = date.getDate();
  const month = date.getMonth() + 1;
  const year = date.getFullYear();

  return lang === 'en' ? `${month}.${day}.${year}` : `${day}.${month}.${year}`;
};

export const getCity = (location) => {
  const districtValue = location[district];
  const cityValue = location[city] ?? '';
  const value = `${cityValue} ${districtValue ? `(${districtValue})` : ''}`;
  return cityValue ? value : '';
};
