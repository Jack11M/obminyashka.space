import { combineReducers } from "redux";

import { profileMeReducer } from "./profile";

const rootReducer = combineReducers({
	profileMe: profileMeReducer
});

export default rootReducer;