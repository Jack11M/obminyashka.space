import { createSlice } from '@reduxjs/toolkit';

const uiInitialState = {
  isFetching: false,
};

const uiSlice = createSlice({
  name: 'ui',
  initialState: uiInitialState,
  reducers: {
    setFetching: (state, { payload }) => {
      state.isFetching = payload;
    },
  },
});

export const getFetching = (state) => state.ui.isFetching;

const {
  reducer: uiReducer,
  actions: { setFetching },
} = uiSlice;

export { setFetching, uiReducer, uiInitialState };
