import { types } from './types';

const initialState = {
	isAuthenticated: false,
	logEmail:'',
	logPassword: '',
	logCheckbox: false,
	RegEmail:'',
	RegNick: '',
	RegPassword: '',
	RegConfirm:'',
	Error: '',

};

export const authReducer = (state = initialState, action) => {
	switch (action.type) {
		case types.TYPE:
			return state;

		default:
			return state;
	}
};