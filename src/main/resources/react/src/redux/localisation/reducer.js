import { types } from './types';

const initialState =  localStorage.getItem('lang') || 'ru';

export const languageReducer = (state = initialState, action) => {
	switch (action.type) {
		case types.CHOOSE_LANGUAGE:
			localStorage.setItem('lang', action.payload)
			return action.payload;

		default:
			return state;
	}
};
