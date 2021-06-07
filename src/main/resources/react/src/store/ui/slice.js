import { createSlice } from '@reduxjs/toolkit';


const uiInitialState = {
	isFetching: false
};

const uiSlice = createSlice( {
	name: 'ui',
	initialState: uiInitialState,
	reducers: {
		startFetching: ( state ) => {
			state.isFetching = true;
		},
		stopFetching: ( state ) => {
			state.isFetching = false;
		}
	}
} );

const {
	reducer: uiReducer,
	actions: {
		startFetching,
		stopFetching
	}
} = uiSlice;

export { startFetching, stopFetching, uiReducer, uiInitialState };
