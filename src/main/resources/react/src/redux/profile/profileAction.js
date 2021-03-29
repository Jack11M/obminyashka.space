import { types } from './types';

export const changeInputValue = ( data ) => ({
	type: types.CHANGE_INPUT_VALUE,
	payload: data
});

export const changePhoneInputValue = ( data, id ) => ({
	type: types.CHANGE_PHONE_INPUT_VALUE,
	payload: data,
	id
});

export const deletePhone = ( id ) => ({
	type: types.DELETE_PHONE,
	payload: id
});

export const addPhone = () => ({
	type: types.ADD_PHONE
});

export const changeChildrenInputValue = ( data, id ) => ({
	type: types.CHANGE_CHILDREN_INPUT_VALUE,
	payload: data,
	id
});

export const chooseGender = ( data, id ) => ({
	type: types.CHOOSE_GENDER,
	payload: data,
	id
});

export const deleteChild = ( id ) => ({
	type: types.DELETE_CHILD,
	payload: id
});

export const addChild = () => ({
	type: types.ADD_CHILD
});

export const changeLangProfileErrors = ( lang ) => ({
	type: types.CHANGE_LANG_PROFILE_ERRORS,
	payload: lang
});

export const changePermissionProfile = () => ({
	type: types.CHANGE_PERMISSION_PROFILE
});

export const changePermissionChildren = () => ({
	type: types.CHANGE_PERMISSION_CHILDREN
});


export const fetchUserInfoAsync = () => ({
	type: types.FETCH_USER_INFO_ASYNC
});

export const fillUserInfoSync = ( user ) => ({
	type: types.FILL_USER_INFO_SYNC,
	payload: user
});

export const putUserInfoAsync = ( user ) => ({
	type: types.PUT_USER_INFO_ASYNC,
	payload: user
});

export const postChildrenAsync = ( children ) => ({
	type: types.USER_POST_CHILDREN_ASYNC,
	payload: children
});

export const receivedPostRequestChildrenSync = ( children ) => ({
	type: types.PUT_RECEIVED_POST_CHILDREN,
	payload: children
});

export const putChildrenAsync = ( children ) => ({
	type: types.USER_PUT_CHILDREN_ASYNC,
	payload: children
});

export const receivedPutRequestChildrenSync = ( children ) => ({
	type: types.PUT_RECEIVED_PUT_CHILDREN,
	payload: children
});

export const deleteChildrenAsync = ( id ) => ({
	type: types.USER_DELETE_CHILDREN_ASYNC,
	payload: id
});
