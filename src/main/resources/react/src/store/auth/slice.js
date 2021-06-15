import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { getStorageLang, getStorageUser, removeTokenFromStorage } from '../../Utils';
import { postAuthLogout } from '../../REST/Resources';


const authInitialState = {
	isFetchingAuth: false,
	lang: getStorageLang(),
	isAuthenticated: !!getStorageUser( 'user' ).token,
	username: getStorageUser( 'user' ).username,
	email: getStorageUser( 'user' ).email,
	token: getStorageUser( 'user' ).token,
	avatarImage: getStorageUser( 'user' ).avatarImage,
	firstname: getStorageUser( 'user' ).firstname,
	lastname: getStorageUser( 'user' ).lastname
};

export const fetchLogOut = createAsyncThunk( 'auth/fetchLogOut',
	async ( { dispatch } ) => {
		try {
			await postAuthLogout();
			dispatch( unauthorized() );
		} catch (err) {
			dispatch( unauthorized() );
		}
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
		putEmail: ( state, { payload } ) => {
			state.email = payload;
		},
		setAuthenticated: ( state ) => {
			state.isAuthenticated = true;
		},
		unAuthenticated: ( state ) => {
			state.isAuthenticated = false;
		},

		unauthorized: ( state ) => {
			resetUser(state)
		},
		putToken: ( state, { payload } ) => {
			const { data, isCheck } = payload;
			state.isAuthenticated = true;
			state.username = data.username;
			state.email = data.email;
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
		[fetchLogOut.fulfilled]: ( state, {payload} ) => {
			console.log(payload);
			resetUser(state)

		},
		[fetchLogOut.rejected]: ( state , {payload}) => {
			console.log(payload);
			resetUser(state)
		}
	}
} );
function  resetUser (state) {
	state.isFetchingAuth = false;
	removeTokenFromStorage();
	state.isAuthenticated = '';
	state.email = ''
	state.username = '';
	state.token = '';
	state.avatarImage = '';
	state.firstname = '';
	state.lastname = '';
}

const {
	reducer: authReducer, actions: {
		setLanguage,
		putEmail,
		setAuthenticated,
		unAuthenticated,
		unauthorized,
		putToken
	}
} = authSlice;

export { setLanguage,putEmail,  setAuthenticated, unAuthenticated, unauthorized, putToken, authReducer, authInitialState };
