import { combineReducers } from '@reduxjs/toolkit';
import { authReducer } from '../auth/slice';
import { uiReducer } from '../ui/slice';
import { profileMeReducer } from '../profile/slice';

const rootReducer = combineReducers({
  ui: uiReducer,
  auth: authReducer,
  profileMe: profileMeReducer,
});

export { rootReducer };
