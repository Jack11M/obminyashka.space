import { call, put } from 'redux-saga/effects';

import { deleteUserChildren } from '../../../../REST/Resources';
import { deleteChild } from '../../profileAction';
import { startFetching, stopFetching } from '../../../ui/action';
import { unauthorized } from '../../../auth/action';

export function* workerUserChildrenDelete( action ) {
	const [, id ] = action.payload;

	try {
		yield put( startFetching() );
		yield call( deleteUserChildren, id );
		yield put( deleteChild( action.payload ) );
	} catch (e) {
		if (e.response.status === 401) {
			yield put( unauthorized() );
		}
		console.log( e.response.data );
	} finally {
		yield put( stopFetching() );
	}
}
