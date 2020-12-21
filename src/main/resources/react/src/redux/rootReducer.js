import { combineReducers } from 'redux';

import { authReducer as auth } from './auth/reducer'
import profileMe from './profile/profileReducer';
import { uiReducer as ui } from './ui/reducer';
import { languageReducer as lang } from './localisation/reducer';

const rootReducer = combineReducers( {
	lang,
	auth,
	ui,
	profileMe
} );

export default rootReducer;
