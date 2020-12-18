import { combineReducers } from 'redux';

import profileMe from './profile/profileReducer';
import { uiReducer as ui } from './ui/reducer';
import { languageReducer as lang } from './localisation/reducer';

const rootReducer = combineReducers( {
	lang,
	profileMe,
	ui
} );

export default rootReducer;