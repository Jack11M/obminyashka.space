import { call, put } from 'redux-saga/effects';

import { startFetching, stopFetching } from '../../../ui/action';
import { postUserChildren } from '../../../../REST/Resources';
import { unauthorized } from '../../../auth/action';
import { receivedPostRequestChildrenSync } from '../../profileAction';

export function* workerUserChildrenPost( action ) {
	const body = action.payload;
	yield put( startFetching() );
	try {
		const { data } = yield call( postUserChildren, body );
		yield put( receivedPostRequestChildrenSync( data ) );
	} catch (e) {
		console.log( e.response.data );
		if (e.response.status === 401) {
			yield put( unauthorized() );
		}
	} finally {
		yield put( stopFetching() );
	}
}
