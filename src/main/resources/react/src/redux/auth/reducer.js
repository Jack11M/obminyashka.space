import { types } from './types';
import { authValidation } from '../Utils';
import { getTranslatedText } from '../../components/local/localisation';


const initialState = {
	lang: localStorage.getItem('lang') || 'ru',
	isAuthenticated: false,
	logEmail: {
		value: '',
		error: ''
	},
	logPassword: {
		value: '',
		error: ''
	},

	regEmail: {
		value: '',
		error: ''
	},
	regNick: {
		value: '',
		error: ''
	},
	regPassword: {
		value: '',
		error: ''
	},
	regConfirm: {
		value: '',
		error: ''
	},
	logCheckbox: false,
	regCheckbox: false


};

export const authReducer = ( state = initialState, action ) => {
	switch (action.type) {
		case types.CHOOSE_LANGUAGE:
			localStorage.setItem('lang', action.payload)
			return {
				...state,
				lang: action.payload
			}


		case types.CHANGE_INPUT_AUTH:
			const [ keyInput, value ] = action.payload;
			const Verified = authValidation( state, keyInput, value );
			const newError = Verified ? '' :  getTranslatedText(`errors.${keyInput}`, state.lang)
			return {
				...state,
				[keyInput]: { value, error: newError }
			};


		case types.CLEAR_VALUE_LOGIN:
			return {
				...state,
				logEmail: {
					value: '',
					error: ''
				},
				logPassword: {
					value: '',
					error: ''
				},
				logCheckbox: false
			};

		case types.CLEAR_VALUE_SIGNUP:
			return {
				...state,
				regEmail: {
					value: '',
					error: ''
				},
				regNick: {
					value: '',
					error: ''
				},
				regPassword: {
					value: '',
					error: ''
				},
				regConfirm: {
					value: '',
					error: ''
				},
				regCheckbox: false
			};

		case types.CHANGE_CHECKBOX:
			return {
				...state,
				...action.payload
			};

		default:
			return state;
	}
};