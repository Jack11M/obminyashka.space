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
      state.avatarImage = payload?.avatarImage;
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
      state.avatarImage = payload.avatarImage;
    },
  },
});

export const getProfile = (state) => state.profileMe;

const {
  reducer: profileMeReducer,
  actions: { putUserToStore, putProfile, putChildrenToStore, setAvatar },
} = profileMeSlice;

export {
  setAvatar,
  putProfile,
  putUserToStore,
  profileMeReducer,
  putChildrenToStore,
  profileMeInitialState,
};
