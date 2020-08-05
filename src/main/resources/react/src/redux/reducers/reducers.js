import { combineReducers } from "redux";

import { addMe } from "./profile";

const rootReducer = combineReducers({
  stateProfile: addMe,
});

export default rootReducer;
