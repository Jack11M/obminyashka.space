import { call, put } from 'redux-saga/effects';

import { getUserInfo as getUserInfoAsync } from '../../../../REST/Resources';
import { fillUserInfoSync } from '../../profileAction';
import { startFetching, stopFetching } from '../../../ui/action';
import { unauthorized } from '../../../auth/action';

export function* workerUserInfoGet() {
	yield put( startFetching() );
	try {
		const { data } = yield call( getUserInfoAsync );
		yield put( fillUserInfoSync( data ) );
	} catch (e) {
		if (e.response.status === 401) {
			yield put( unauthorized() );
		}
		console.log( e.response.data.error );
	} finally {
		yield put( stopFetching() );
	}
}
