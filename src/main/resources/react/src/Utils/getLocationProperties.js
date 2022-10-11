import { getStorageLang } from './storage';

const lang = getStorageLang();

export const area = `area${lang.toUpperCase()}`;
export const city = `city${lang.toUpperCase()}`;
export const district = `district${lang.toUpperCase()}`;
