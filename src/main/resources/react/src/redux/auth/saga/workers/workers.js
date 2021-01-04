import { call, put } from 'redux-saga/effects';

import { startFetching, stopFetching } from '../../../ui/action';
import { postAuthLogin, postAuthRegister } from '../../../../REST/Resources';
import { putTokenLocalStorage, showErrorLogin, showErrorRegister, successReg } from '../../action';

export function* workerPostAuthLogin( action ) {
	const body = action.payload;
	try {
		yield put( startFetching() );
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
	try {
		yield put( startFetching() );
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
