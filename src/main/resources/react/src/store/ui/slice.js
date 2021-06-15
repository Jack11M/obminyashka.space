import { createSlice } from '@reduxjs/toolkit';


const uiInitialState = {
	isFetching: false
};

const uiSlice = createSlice( {
	name: 'ui',
	initialState: uiInitialState,
	reducers: {
		setFetching: ( state, {payload} ) => {
			state.isFetching = payload;
		}
	}
} );

const {
	reducer: uiReducer,
	actions: {
		setFetching
	}
} = uiSlice;

export { setFetching, uiReducer, uiInitialState };
