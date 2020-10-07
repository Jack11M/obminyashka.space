import { call, put } from 'redux-saga/effects';

import { getUserInfo as getUserInfoAsync } from '../../../../REST/Resources';
import { fillUserInfoSync } from '../../profileAction';
import {startFetching, stopFetching} from '../../../ui/action'

export function* userInfoGet() {
	try{
		yield put(startFetching())
		const {data} = yield call(getUserInfoAsync);
		yield put(fillUserInfoSync(data));
	}catch (e) {
		console.log(e.response.data.error)
	}finally {
		yield put(stopFetching())
	}
}