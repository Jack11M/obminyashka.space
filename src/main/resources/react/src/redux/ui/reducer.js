import { types } from './types';

const initialState = {
	isFetching: false
};

export const uiReducer = (state = initialState, action) => {
	switch (action.type) {
		case types.START_FETCHING:
			return {
				...state,
				isFetching: true
			};

		case types.STOP_FETCHING:
			return {
				...state,
				isFetching: false
			};

		default:
			return state;
	}
};