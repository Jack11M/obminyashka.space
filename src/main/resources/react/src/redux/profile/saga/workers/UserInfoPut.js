import { call, put } from 'redux-saga/effects';

import { putUserInfo as putUserInfoAsync } from '../../../../REST/Resources';
import { fillUserInfoSync } from '../../profileAction';
import { startFetching, stopFetching } from '../../../ui/action';

export function* workerUserInfoPut(action) {
	try {
		yield put(startFetching())

		const {data} = yield call(putUserInfoAsync, action.payload);
		console.log(data);
		yield put(fillUserInfoSync(data));
	} catch (e) {
		console.log(e.response.data);
	}finally {
		yield put(stopFetching())
	}
}
