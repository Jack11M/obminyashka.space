import { all, call, takeEvery } from 'redux-saga/effects';

import { types } from '../types';
import { workerPostAuthLogin, workerPostAuthLogout, workerPostAuthRegister } from './workers';

function* watchAuthLogin() {
	yield takeEvery( types.POST_AUTH_LOGIN_ASYNC, workerPostAuthLogin );
}

function* watchAuthRegister() {
	yield takeEvery( types.POST_AUTH_REGISTER_ASYNC, workerPostAuthRegister );
}

function* watchAuthLogout() {
	yield takeEvery( types.POST_AUTH_LOGOUT_ASYNC, workerPostAuthLogout );
}

export function* watchAuth() {
	yield  all( [ call( watchAuthLogin ), call( watchAuthRegister ), call( watchAuthLogout ) ] );
}
