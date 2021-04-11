import { call, put } from 'redux-saga/effects';

import { startFetching, stopFetching } from '../../../ui/action';
import { postAuthLogin, postAuthLogout, postAuthRegister } from '../../../../REST/Resources';
import { putTokenLocalStorage, showErrorLogin, showErrorRegister, successReg, unauthorized } from '../../action';

export function* workerPostAuthLogin( action ) {
	const body = action.payload;
	yield put( startFetching() );
	try {
		const { data } = yield call( postAuthLogin, body );
		yield put( putTokenLocalStorage( data ) );
	} catch (e) {
		if (e.response.status === 400) {
			yield put( showErrorLogin( e.response.data.error ) );
		}
		console.log( e.response.data.error );
	} finally {
		yield put( stopFetching() );
	}
}

export function* workerPostAuthRegister( action ) {
	const body = action.payload;
	yield put( startFetching() );
	try {
		yield call( postAuthRegister, body );
		yield put( successReg() );
	} catch (e) {
		if (e.response.status === 400) {
			yield put( showErrorRegister( e.response.data.error ) );
		}
		console.log( e.response.data.error );
	} finally {
		yield put( stopFetching() );
	}
}

export function* workerPostAuthLogout() {
	yield put( startFetching() );
	try {
		yield call( postAuthLogout );
		yield put( unauthorized() );
	} catch (e) {
		console.log( e.response.data.error );
		if (e.response.status === 401) {
			yield put( unauthorized() );
		}
	} finally {
		yield put( stopFetching() );
	}
}
