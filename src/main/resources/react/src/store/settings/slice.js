import { createSlice } from '@reduxjs/toolkit';


const settingsInitialState = {
	isFetchPass :  false,
	isFetchEmail :  false,
	isFetchRemove :  false,

};

const settingsSlice = createSlice( {
	name: 'profile',
	initialState: settingsInitialState,
	reducers: {
		startFetching: ( state ) => {
			state.isFetching = true;
		},
		stopFetching: ( state ) => {
			state.isFetching = false;
		},
	}
} );

const {
	reducer: settingsReducer,
	actions: {
		startFetching,
		stopFetching
	}
} = settingsSlice;

export { startFetching, stopFetching, settingsReducer, settingsInitialState };
