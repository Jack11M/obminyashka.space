import { createSlice } from '@reduxjs/toolkit';
import { showMessage } from 'obminyashka-components';

import { getStorageLang, getStorageUser, setStorageUser } from 'Utils';

const authInitialState = {
  profile: getStorageUser('user'),
  isFetchingAuth: false,
  lang: getStorageLang(),
  isChangeLang: false,
  isAuthed: !!getStorageUser('user'),
};

const authSlice = createSlice({
  name: 'auth',
  initialState: authInitialState,
  reducers: {
    setLanguage: (state, { payload }) => {
      try {
        setStorageUser({ 'Accept-Language': payload });
        localStorage.setItem('lang', payload);
        state.lang = payload;
      } catch (e) {
        showMessage.error('You need to enable a localStorage');
      }
    },
    setChangeLang: (state, { payload }) => {
      state.isChangeLang = payload;
    },
    putEmail: (state, { payload }) => {
      setStorageUser({ email: payload });
      state.profile.email = payload;
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
    putEmail,
    putToken,
    setAuthed,
    setLoader,
    logOutUser,
    setLanguage,
    setChangeLang,
  },
} = authSlice;

export const getAuth = (state) => state.auth;
export const getAuthed = (state) => state.auth.isAuthed;
export const getAuthLang = (state) => state.auth.lang;
export const getAuthProfile = (state) => state.auth.profile;

export {
  putEmail,
  putToken,
  setAuthed,
  setLoader,
  logOutUser,
  setLanguage,
  authReducer,
  setChangeLang,
  authInitialState,
};
