import { combineReducers } from '@reduxjs/toolkit';

import { uiReducer } from '../ui/slice';
import { advReducer } from '../adv/slice';
import { authReducer } from '../auth/slice';
import { profileMeReducer } from '../profile/slice';

const rootReducer = combineReducers({
  ui: uiReducer,
  auth: authReducer,
  profileMe: profileMeReducer,
  adv: advReducer,
});

export { rootReducer };
