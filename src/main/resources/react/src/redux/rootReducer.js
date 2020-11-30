import { combineReducers } from 'redux';

import profileMe from './profile/profileReducer';
import { uiReducer as ui } from'./ui/reducer'

const rootReducer = combineReducers({
	profileMe,
	ui
});

export default rootReducer;