import { call, put } from 'redux-saga/effects';

import { putUserInfo as putUserInfoAsync } from '../../../../REST/Resources';
import { fillUserInfoSync } from '../../profileAction';
import { startFetching, stopFetching } from '../../../ui/action';

export function* userInfoPut(action) {
	const body = action.payload;
	try {
		yield put(startFetching())
		yield call(putUserInfoAsync, body);
		yield put(fillUserInfoSync(body));
	} catch (e) {
		console.log(e.response.data);
	}finally {
		yield put(stopFetching())
	}
}