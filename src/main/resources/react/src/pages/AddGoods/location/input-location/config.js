import { store } from 'store';

const { lang } = store.getState().auth;

export const area = `area${lang.toUpperCase()}`;
export const city = `city${lang.toUpperCase()}`;
export const district = `district${lang.toUpperCase()}`;
