import { types } from './types';

export const chooseLanguage = (lang) => ({
	type: types.CHOOSE_LANGUAGE,
	payload: lang
});

export const clearValueLogin  = () => ({
	type: types.CLEAR_VALUE_LOGIN
});

export const clearValueSignUp  = () => ({
	type: types.CLEAR_VALUE_SIGNUP
});

export const inputChangeAuth = ( input ) => ({
	type: types.CHANGE_INPUT_AUTH,
	payload: input
});

export const toggleCheckBox = ( toggle ) => ({
	type: types.CHANGE_CHECKBOX,
	payload: toggle
});

export const toggleDisableButtonLog  = () => ({
	type: types.TOGGLE_DISABLE_BUTTON_LOG
});

export const toggleDisableButtonReg  = () => ({
	type: types.TOGGLE_DISABLE_BUTTON_REG
});

export const postAuthLoginAsync = ( body ) => ({
	type: types.POST_AUTH_LOGIN_ASYNC,
	payload: body
});

export const postAuthRegisterAsync = ( body ) => ({
	type: types.POST_AUTH_REGISTER_ASYNC,
	payload: body
});

export const putTokenLocalStorage = ( body ) => ({
	type: types.PUT_TOKEN_LOCALSTORAGE,
	payload: body
});

export const showErrorLogin  = (body) => ({
	type: types.SHOW_ERROR_LOGIN,
	payload: body
});

export const showErrorRegister  = (body) => ({
	type: types.SHOW_ERROR_REGISTER,
	payload: body
});

export const successReg  = () => ({
	type: types.SUCCESS_REGISTER
});