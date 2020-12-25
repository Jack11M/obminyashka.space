import { types } from './types';
import { authValidation, getStorage, getStorageLang, toggleButtonLog, toggleButtonReg } from '../Utils';
import { getTranslatedText } from '../../components/local/localisation';


const initialState = {
	lang: getStorageLang(),
	isAuthenticated: !!getStorage( 'token' ),
	username: getStorage( 'username' ),
	token: getStorage( 'token' ),
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
	regCheckbox: false,
	disableLog: true,
	disableReg: true
};

export const authReducer = ( state = initialState, action ) => {
	switch (action.type) {
		case types.CHOOSE_LANGUAGE:
			localStorage.setItem( 'lang', action.payload );
			return {
				...state,
				lang: action.payload
			};


		case types.CHANGE_INPUT_AUTH:
			const [ keyInput, value ] = action.payload;
			const Verified = authValidation( state, keyInput, value );
			const newError = Verified ? '' : getTranslatedText( `errors.${ keyInput }`, state.lang );
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
				logCheckbox: false,
				disableLog: true
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
				regCheckbox: false,
				disableReg: true
			};

		case types.CHANGE_CHECKBOX:
			return {
				...state,
				...action.payload
			};

		case types.TOGGLE_DISABLE_BUTTON_LOG:
			return {
				...state,
				disableLog: !toggleButtonLog( state )
			};

		case types.TOGGLE_DISABLE_BUTTON_REG:
			return {
				...state,
				disableReg: !toggleButtonReg( state )
			};

		case types.PUT_TOKEN_LOCALSTORAGE:
			if (state.logCheckbox) {
				localStorage.setItem( 'user', JSON.stringify( action.payload ) );
			} else {
				sessionStorage.setItem( 'user', JSON.stringify( action.payload ) );
			}
			const { username, token } = action.payload;
			return {
				...state,
				isAuthenticated: true,
				username,
				token
			};
		default:
			return state;
	}
};