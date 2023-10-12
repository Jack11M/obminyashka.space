/* eslint-disable */
// @ts-nocheck
// TODO: fix typescript
import { createSlice } from '@reduxjs/toolkit';
import { showMessage } from 'obminyashka-components';

import { getStorageLang, getStorageUser, addDataToUserInStorage } from 'src/Utils';

const authInitialState = {
  isFetchingAuth: false,
  lang: getStorageLang(),
  profile: getStorageUser('user'),
  isAuthed: !!getStorageUser('user'),
};

const authSlice = createSlice({
  name: 'auth',
  initialState: authInitialState,
  reducers: {
    setLanguage: (state, { payload }) => {
      try {
        addDataToUserInStorage({ 'Accept-Language': payload });
        localStorage.setItem('lang', payload);
        state.lang = payload;
      } catch (e) {
        showMessage.error('You need to enable a localStorage');
      }
    },
    putEmail: (state, { payload }) => {
      addDataToUserInStorage({ email: payload });
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
  actions: { putEmail, putToken, setAuthed, setLoader, logOutUser, setLanguage },
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
  authInitialState,
};
