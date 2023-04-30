/* eslint-disable */
// @ts-nocheck
// TODO: fix typescript
import { createSlice } from '@reduxjs/toolkit';

const profileMeInitialState = {
  profile: null,
};

const profileMeSlice = createSlice({
  name: 'profile',
  initialState: profileMeInitialState,
  reducers: {
    putUserToStore: (state, { payload }) => {
      state.profile.firstName = payload.firstName;
      state.profile.lastName = payload.lastName;
      state.profile.phones = payload.phones;
    },
    putChildrenToStore: (state, { payload }) => {
      state.profile.children = payload;
    },
    putProfile: (state, { payload }) => {
      state.profile = payload;
    },
    setAvatar: (state, { payload }) => {
      state.profile.avatarImage = payload;
    },
    setProfileEmail: (state, { payload }) => {
      state.profile.email = payload;
    },
    clearProfile: (state) => {
      state.profile = null;
    },
  },
});

export const getProfile = (state) => state.profileMe.profile;

const {
  reducer: profileMeReducer,
  actions: {
    setAvatar,
    putProfile,
    clearProfile,
    putUserToStore,
    setProfileEmail,
    putChildrenToStore,
  },
} = profileMeSlice;

export {
  setAvatar,
  putProfile,
  clearProfile,
  putUserToStore,
  setProfileEmail,
  profileMeReducer,
  putChildrenToStore,
  profileMeInitialState,
};
