import { city, district } from 'Utils';

export const getCity = (location) => {
  const districtValue = location[district];
  const cityValue = location[city] ?? '';
  const value = `${cityValue} ${districtValue ? `(${districtValue})` : ''}`;
  return cityValue ? value : '';
};
