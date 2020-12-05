import { types } from './types';

export const startFetching = () => ({
	type: types.START_FETCHING
});
export const stopFetching = () => ({
	type: types.STOP_FETCHING
});