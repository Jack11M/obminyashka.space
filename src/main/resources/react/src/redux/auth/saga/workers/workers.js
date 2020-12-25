import { call, put } from 'redux-saga/effects';

import { startFetching, stopFetching } from '../../../ui/action';
import { postAuthLogin, postAuthRegister } from '../../../../REST/Resources';
import { putTokenLocalStorage } from '../../action';

export function* workerPostAuthLogin(action) {
	const body = action.payload;
	try {
		yield put( startFetching() );
		const { data } = yield call( postAuthLogin, body );
		console.log(data);
		yield put( putTokenLocalStorage( data ) );
	} catch (e) {
		console.log( e.response.data.error );
	} finally {
		yield put( stopFetching() );
	}
}

export function* workerPostAuthRegister(action) {
	const body = action.payload;
	try {
		yield put( startFetching() );
		const { data } = yield call( postAuthRegister, body );
		console.log(data);
		// yield put( fillUserInfoSync( data ) );
	} catch (e) {
		console.log( e.response.data.error );
	} finally {
		yield put( stopFetching() );
	}
}