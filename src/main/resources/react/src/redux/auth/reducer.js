import { types } from './types';

const initialState = {
	isAuthenticated: false,
	logEmail:{
		error: '',
	},
	logPassword: {
		error: '',
	},

	regEmail: {
		error: '',
	},
	regNick: {
		error: '',
	},
	regPassword: {
		error: '',
	},
	regConfirm: {
		error: '',
	},
	logCheckbox: false,
	regCheckbox: false,


};

export const authReducer = (state = initialState, action) => {
	switch (action.type) {
		case types.CHANGE_CHECKBOX:
			console.log( state[action.payload]);
			console.log( state.logCheckbox);
			return {
				...state,
				...action.payload
			}
			default:
			return state;
	}
};