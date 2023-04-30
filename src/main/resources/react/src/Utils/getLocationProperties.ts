/* eslint-disable */
// @ts-nocheck
// TODO: fix typescript
import { getStorageLang } from './storage';

const lang = getStorageLang();

export const area = `area${lang.toUpperCase()}`;
export const city = `city${lang.toUpperCase()}`;
export const district = `district${lang.toUpperCase()}`;

export const getCity = (location) => {
  const districtValue = location?.[district];
  const cityValue = location?.[city] ?? '';
  const value = `${cityValue} ${districtValue ? `(${districtValue})` : ''}`;
  return cityValue ? value : '';
};
