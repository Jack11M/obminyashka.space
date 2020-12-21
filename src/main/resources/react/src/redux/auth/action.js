import { types } from './types';

export const toggleCheckBox = ( toggle) => ({
	type: types.CHANGE_CHECKBOX,
	payload: toggle
});