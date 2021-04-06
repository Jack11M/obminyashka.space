import { types } from './types';
import { authValidation, getStorageLang, getStorageUser, removeTokenFromStorage, toggleButtonLog, toggleButtonReg } from '../Utils';
import { getTranslatedText } from '../../components/local/localisation';



const initialState = {
	lang: getStorageLang(),
	isAuthenticated: !!getStorageUser( 'token' ),
	username: getStorageUser( 'username' ),
	token: getStorageUser( 'token' ),
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
	disableReg: true,
	successRegister: false
};

export const authReducer = ( state = initialState, action ) => {
	switch (action.type) {
		case types.CHOOSE_LANGUAGE:
			try {
				localStorage.setItem( 'lang', action.payload );
			} catch (e) {
				console.log( e );
				console.log( 'You need to enable a localStorage' );
			}
			return {
				...state,
				lang: action.payload
			};


		case types.CHANGE_INPUT_AUTH:
			const [ keyInput, value ] = action.payload;
			const verified = authValidation( state, keyInput, value );
			const newError = verified ? '' : getTranslatedText( `errors.${ keyInput }`, state.lang );
			return {
				...state,
				[keyInput]: { value, error: newError }
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
			try {
				if (state.logCheckbox) {
					localStorage.setItem( 'user', JSON.stringify( action.payload ) );
				} else {
					sessionStorage.setItem( 'user', JSON.stringify( action.payload ) );
				}
			} catch (e) {
				console.log( e );
				console.log( 'You need to enable a localStorage' );
			}
			const { username, token } = action.payload;
			return {
				...state,
				isAuthenticated: true,
				username,
				token
			};

		case types.SHOW_ERROR_LOGIN:
			return {
				...state,
				logEmail: { ...state.logEmail, error: action.payload }
			};

		case types.SHOW_ERROR_REGISTER:
			const nameError = action.payload.includes( 'login' ) || action.payload.includes( 'имя' );
			const errorFromReg = nameError ? { regNick: { ...state.regNick, error: action.payload } } : {
				regEmail: {
					...state.regEmail,
					error: action.payload
				}
			};

			return {
				...state,
				...errorFromReg
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
				disableLog: true,
				successRegister: false
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

		case types.SUCCESS_REGISTER:
			return {
				...state,
				successRegister: true
			};

		case types.UNAUTHORIZED:
			return {
				...state,
				isAuthenticated: removeTokenFromStorage(),
				token: ''
			};

		default:
			return state;
	}
};
