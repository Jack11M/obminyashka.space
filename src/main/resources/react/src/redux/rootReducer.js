import { combineReducers } from 'redux';

import { authReducer as auth } from './auth/reducer';
import profileMe from './profile/profileReducer';
import { uiReducer as ui } from './ui/reducer';

const rootReducer = combineReducers( {
	auth,
	ui,
	profileMe
} );

export default rootReducer;
