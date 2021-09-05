import { createSlice } from '@reduxjs/toolkit';
import { getStorageLang, getStorageUser } from 'Utils';

const authInitialState = {
  profile: getStorageUser('user'),
  isFetchingAuth: false,
  lang: getStorageLang(),
  isAuthed: !!getStorageUser('user'),
};

const authSlice = createSlice({
  name: 'auth',
  initialState: authInitialState,
  reducers: {
    setLanguage: (state, { payload }) => {
      try {
        localStorage.setItem('lang', payload);
        state.lang = payload;
      } catch (e) {
        console.log(e);
        console.log('You need to enable a localStorage');
      }
    },
    putEmail: (state, { payload }) => {
      state.email = payload;
    },
    setAuthed: (state, { payload }) => {
      state.isAuthenticated = payload;
    },
    setLoader: (state, { payload }) => {
      state.isFetchingAuth = payload;
    },
    logOutUser: (state) => {
      state.profile = '';
      state.isFetchingAuth = false;
      state.isAuthed = false;
      localStorage.removeItem('user');
      sessionStorage.removeItem('user');
    },
    putToken: (state, { payload }) => {
      state.profile = payload;
      state.isAuthed = true;
    },
  },
});

const {
  reducer: authReducer,
  actions: {
    setLanguage,
    putEmail,
    setAuthed,
    setLoader,
    logOutUser,
    putToken,
  },
} = authSlice;

export {
  setLanguage,
  putEmail,
  setAuthed,
  setLoader,
  logOutUser,
  putToken,
  authReducer,
  authInitialState,
};
