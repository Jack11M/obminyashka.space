import {all, call} from 'redux-saga/effects';

import { watchProfile } from './profile/saga/watchers';

export function* rootSaga() {
	yield all([call(watchProfile)]);
}
