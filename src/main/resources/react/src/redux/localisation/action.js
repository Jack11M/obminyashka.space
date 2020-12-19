import { types } from './types';

export const chooseLanguage = (lang) => ({
	type: types.CHOOSE_LANGUAGE,
	payload: lang
});
