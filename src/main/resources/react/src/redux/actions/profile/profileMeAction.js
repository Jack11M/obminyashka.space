import { types } from '../../types'

export const addMeInputValue = (data) => ({
	type: types.ADD_ME_INPUT_VALUE,
	payload: data
});

export const addChildrenInputValue = (data, id) => ({
	type: types.ADD_CHILDREN_INPUT_VALUE,
	payload: data,
	id
});

export const deleteChild = (id) => ({
	type: types.DELETE_CHILD,
	payload: id
});

export const addChild = () => ({
	type: types.ADD_CHILD
});