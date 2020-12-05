import { all, call, takeEvery } from 'redux-saga/effects';

import { types } from '../types';
import { userInfoGet, userInfoPut } from './workers';

function* watchGetUserInfo() {
	yield takeEvery(types.FETCH_USER_INFO_ASYNC, userInfoGet);
}

function* watchPutUserInfo() {
	yield takeEvery(types.PUT_USER_INFO_ASYNC, userInfoPut);
}

export function* watchProfile() {
	yield  all([call(watchGetUserInfo), call(watchPutUserInfo)]);
}