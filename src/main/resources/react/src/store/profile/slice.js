import { createSlice } from '@reduxjs/toolkit';

const profileMeInitialState = {
  userLoading: false,
  childrenLoading: false,
  avatarImage: '',
  children: [],
  email: '',
  firstName: '',
  lastName: '',
  lastOnlineTime: '',
  online: false,
  phones: [],
  status: '',
  updated: '',
  username: '',
};

const profileMeSlice = createSlice({
  name: 'profile',
  initialState: profileMeInitialState,
  reducers: {
    putUserToStore: (state, { payload }) => {
      state.firstName = payload.firstName;
      state.lastName = payload.lastName;
      state.phones = payload.phones;
    },
    putChildrenToStore: (state, { payload }) => {
      state.children = payload;
    },
    putProfile: (state, { payload }) => {
      state.avatarImage = payload?.avatarImage || '';
      state.children = payload?.children;
      state.email = payload?.email;
      state.firstName = payload?.firstName;
      state.lastName = payload?.lastName;
      state.lastOnlineTime = payload?.lastOnlineTime;
      state.online = payload?.online;
      state.phones = payload?.phones;
      state.status = payload?.status;
      state.updated = payload?.updated;
      state.username = payload?.username;
    },
    setAvatar: (state, { payload }) => {
      state.avatarImage = payload;
    },
    setProfileEmail: (state, { payload }) => {
      state.email = payload;
    },
    clearProfile: (state) => {
      state.userLoading = false;
      state.childrenLoading = false;
      state.avatarImage = '';
      state.children = [];
      state.email = '';
      state.firstName = '';
      state.lastName = '';
      state.lastOnlineTime = '';
      state.online = false;
      state.phones = [];
      state.status = '';
      state.updated = '';
      state.username = '';
    },
  },
});

export const getProfile = (state) => state.profileMe;

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
