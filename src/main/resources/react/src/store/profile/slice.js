import { createSlice } from '@reduxjs/toolkit';


const profileMeInitialState = {
	avatarImage: '',
	firstName: '',
	lastName: '',
	username: '',
	email: '',
	phones: [],
	children: [],
	online: '',
	lastOnlineTime: '',
	mockPhones: [
		{
			defaultPhone: false,
			id: 0,
			phoneNumber: ''
		}
	],
	mockChildren: [
		{
			birthDate: '',
			id: 0,
			sex: 'UNSELECTED'
		}
	],
	errors: [],
	errorsPhone: [],
	errorsChildren: [],
	prohibitionToSendAboutMe: true,
	prohibitionToSendChildren: true,
	receivedChildrenFromBack: []
};

const profileMeSlice = createSlice( {
	name: 'profile',
	initialState: profileMeInitialState,
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
	reducer: profileMeReducer,
	actions: {
		startFetching,
		stopFetching
	}
} = profileMeSlice;

export { startFetching, stopFetching, profileMeReducer, profileMeInitialState };
