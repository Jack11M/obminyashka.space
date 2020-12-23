import { types } from './types';

export const chooseLanguage = (lang) => ({
	type: types.CHOOSE_LANGUAGE,
	payload: lang
});

export const clearValueLogin  = () => ({
	type: types.CLEAR_VALUE_LOGIN,
});

export const clearValueSignUp  = () => ({
	type: types.CLEAR_VALUE_SIGNUP,
});

export const inputChangeAuth = ( input ) => ({
	type: types.CHANGE_INPUT_AUTH,
	payload: input
});

export const toggleCheckBox = ( toggle ) => ({
	type: types.CHANGE_CHECKBOX,
	payload: toggle
});
