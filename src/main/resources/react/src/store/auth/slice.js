import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { getStorageLang, getStorageUser, removeTokenFromStorage } from '../../Utils';
import { postAuthLogout } from '../../REST/Resources';


const authInitialState = {
	isFetchingAuth: false,
	lang: getStorageLang(),
	isAuthenticated: !!getStorageUser( 'user' ).token,
	username: getStorageUser( 'user' ).username,
	token: getStorageUser( 'user' ).token,
	avatarImage: getStorageUser( 'user' ).avatarImage,
	firstname: getStorageUser( 'user' ).firstname,
	lastname: getStorageUser( 'user' ).lastname
};

export const fetchLogOut = createAsyncThunk( 'auth/fetchLogOut',
	async () => {
		const { data } = await postAuthLogout();
		return data;
	} );

const authSlice = createSlice( {
	name: 'auth',
	initialState: authInitialState,
	reducers: {
		setLanguage: ( state, { payload } ) => {
			try {
				localStorage.setItem( 'lang', payload );
				state.lang = payload;
			} catch (e) {
				console.log( e );
				console.log( 'You need to enable a localStorage' );
			}
		},
		setAuthenticate: ( state, { payload } ) => {
			state.isAuthenticated = payload;
		},
		putToken: ( state, { payload } ) => {
			const { data, isCheck } = payload;
			state.isAuthenticated = true;
			state.username = data.username;
			state.token = data.token;
			state.avatarImage = data.avatarImage;
			state.firstname = data.firstname;
			state.lastname = data.lastname;
			if (isCheck) {
				localStorage.setItem( 'user', JSON.stringify( data ) );
			} else {
				sessionStorage.setItem( 'user', JSON.stringify( data ) );
			}
		}

	},
	extraReducers: {
		[fetchLogOut.pending]: ( state ) => {
			state.isFetchingAuth = true;
		},
		[fetchLogOut.fulfilled]: ( state ) => {
			state.isFetchingAuth = false;
			state.isAuthenticated = false;
			removeTokenFromStorage();
			removeTokenFromStorage();
			state.username = '';
			state.token = '';
			state.avatarImage = '';
			state.firstname = '';
			state.lastname = '';
		},
		[fetchLogOut.rejected]: ( state ) => {
			state.isFetchingAuth = false;
			state.isAuthenticated = false;
			removeTokenFromStorage();
			state.username = '';
			state.token = '';
			state.avatarImage = '';
			state.firstname = '';
			state.lastname = '';
		}
	}
} );

const { reducer: authReducer, actions: { setLanguage, setAuthenticate, putToken } } = authSlice;

export { setLanguage, setAuthenticate, putToken, authReducer, authInitialState };
