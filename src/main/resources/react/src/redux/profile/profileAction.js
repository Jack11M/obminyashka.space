import { types } from './types';

export const addMeInputValue = (data) => ({
	type: types.ADD_ME_INPUT_VALUE,
	payload: data
});

export const addPhoneInputValue = (data, id) => ({
	type: types.ADD_PHONE_INPUT_VALUE,
	payload: data,
	id
});

export const deletePhone = (id) => ({
	type: types.DELETE_PHONE,
	payload: id
});

export const addPhone = () => ({
	type: types.ADD_PHONE
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

export const fetchUserInfoAsync = () => ({
	type: types.FETCH_USER_INFO_ASYNC
});

export const putUserInfoAsync = (user) => ({
	type: types.PUT_USER_INFO_ASYNC,
	payload: user
});

export const fillUserInfoSync = (user) => ({
	type: types.FILL_USER_INFO_SYNC,
	payload: user
});

export const toggleModal = () => ({
  type: types.OPEN_EXIT_MODAL,
})

export const checkModal = (data) => ({
  type: types.CHECK_FIRST_OPENING,
  modalIsOpening: data,
})
