import { call, put } from 'redux-saga/effects';

import { putUserInfo as putUserInfoAsync } from '../../../../REST/Resources';
import { fillUserInfoSync } from '../../profileAction';
import { startFetching, stopFetching } from '../../../ui/action';

export function* userInfoPut(action) {
	const body = action.payload;
	try {
		yield put(startFetching())

		const {data} = yield call(putUserInfoAsync, body);
		console.log(data);
		yield put(fillUserInfoSync(body));
	} catch (e) {
		console.log(e.response.data);
	}finally {
		yield put(stopFetching())
	}
}