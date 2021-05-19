import { call, put } from 'redux-saga/effects';

import { putUserInfo as putUserInfoAsync } from '../../../../REST/Resources';
import { fillUserInfoSync } from '../../profileAction';
import { startFetching, stopFetching } from '../../../ui/action';
import { unauthorized } from '../../../auth/action';

export function* workerUserInfoPut( action ) {
	yield put( startFetching() );
	try {
		const { data } = yield call( putUserInfoAsync, action.payload );
		yield put( fillUserInfoSync( data ) );
	} catch (e) {
		console.log( e.response.data );
		if (e.response.status === 401) {
			yield put( unauthorized() );
		}
	} finally {
		yield put( stopFetching() );
	}
}
