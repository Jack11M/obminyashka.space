import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import api from 'REST/Resources';

export const fetchUser = createAsyncThunk(
  'profileMe/fetchUser',
  async (_, { dispatch }) => {
    try {
      const { data } = await api.fetchProfile.getUserInfo();
      return data;
    } catch (err) {}
  }
);

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
  },
  extraReducers: {
    [fetchUser.fulfilled]: (state, { payload }) => {
      state.avatarImage = payload.avatarImage;
      state.children = payload.children;
      state.email = payload.email;
      state.firstName = payload.firstName;
      state.lastName = payload.lastName;
      state.lastOnlineTime = payload.lastOnlineTime;
      state.online = payload.online;
      state.phones = payload.phones;
      state.status = payload.status;
      state.updated = payload.updated;
      state.username = payload.username;
    },
    [fetchUser.rejected]: (state) => {
      console.log(state);
    },
  },
});

const {
  reducer: profileMeReducer,
  actions: { putUserToStore },
} = profileMeSlice;

export { putUserToStore, profileMeReducer, profileMeInitialState };
