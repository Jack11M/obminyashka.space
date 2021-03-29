import { all, call, takeEvery } from 'redux-saga/effects';

import { types } from '../types';
import { workerUserChildrenDelete, workerUserChildrenPost, workerUserChildrenPut, workerUserInfoGet, workerUserInfoPut } from './workers';

function* watchGetUserInfo() {
	yield takeEvery( types.FETCH_USER_INFO_ASYNC, workerUserInfoGet );
}

function* watchPutUserInfo() {
	yield takeEvery( types.PUT_USER_INFO_ASYNC, workerUserInfoPut );
}

function* watchPostUserChildren() {
	yield takeEvery( types.USER_POST_CHILDREN_ASYNC, workerUserChildrenPost );
}

function* watchPutUserChildren() {
	yield takeEvery( types.USER_PUT_CHILDREN_ASYNC, workerUserChildrenPut );
}

function* watchDeleteUserChildren() {
	yield takeEvery( types.USER_DELETE_CHILDREN_ASYNC, workerUserChildrenDelete );
}


export function* watchProfile() {
	yield  all( [ call( watchGetUserInfo ), call( watchPutUserInfo ), call( watchPostUserChildren ), call( watchPutUserChildren ), call( watchDeleteUserChildren ) ] );
}
