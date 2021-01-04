import { all, call, takeEvery } from 'redux-saga/effects';

import { types } from '../types';
import { workerPostAuthLogin, workerPostAuthRegister } from './workers';

function* watchAuthLogin() {
	yield takeEvery( types.POST_AUTH_LOGIN_ASYNC, workerPostAuthLogin );
}

function* watchAuthRegister() {
	yield takeEvery( types.POST_AUTH_REGISTER_ASYNC, workerPostAuthRegister );
}

export function* watchAuth() {
	yield  all( [ call( watchAuthLogin ), call( watchAuthRegister ) ] );
}
