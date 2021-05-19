import { call, put } from 'redux-saga/effects';

import { deleteUserChildren } from '../../../../REST/Resources';
import { deleteChild } from '../../profileAction';
import { startFetching, stopFetching } from '../../../ui/action';
import { unauthorized } from '../../../auth/action';

export function* workerUserChildrenDelete( action ) {
	const [ , id ] = action.payload;
	yield put( startFetching() );
	try {
		yield call( deleteUserChildren, id );
		yield put( deleteChild( action.payload ) );
	} catch (e) {
		console.log( e.response.data );
		if (e.response.status === 401) {
			yield put( unauthorized() );
		}
	} finally {
		yield put( stopFetching() );
	}
}
