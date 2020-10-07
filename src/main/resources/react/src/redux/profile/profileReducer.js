import { types } from './types';
import { addChild, addInputPhoneOrChildren, deleteChild } from '../Utils/';

const initialState = {
		profile: {
			avatarImage: '',
			firstName: '',
			lastName: '',
			username: '',
			email: '',
			phones: [
				{
					'defaultPhone': false,
					'id': 0,
					'phoneNumber': '',
					'show': true
				},
				{
					'defaultPhone': true,
					'id': 1,
					'phoneNumber': '',
					'show': true
				}
			],
			children: [
				{
					'birthDate': '',
					'id': 0,
					'sex': ''
				}
			],
			online: '',
			lastOnlineTime: ''
		}
	}
;

const profileReducer = (state = initialState, {type, payload, id}) => {
	switch (type) {

		case types.FILL_USER_INFO_SYNC:
			return {
				...state,
				profile: payload
			};

		case types.ADD_ME_INPUT_VALUE:
			return {
				...state,
				profile: {...state.profile, ...payload}
			};

		case types.ADD_PHONE_INPUT_VALUE:
			return {
				...state,
				profile: {...state.profile, phones: addInputPhoneOrChildren(state.profile.phones, payload, id)}
			};

		case types.ADD_CHILDREN_INPUT_VALUE: {
			return {
				...state,
				profile: {...state.profile, children: addInputPhoneOrChildren(state.profile.children, payload, id)}
			};
		}

		case types.ADD_CHILD:
			return {
				...state,
				profile: {...state.profile, children: addChild(state.profile.children)}
			};

		case types.DELETE_CHILD:
			return {
				...state,
				profile: {...state.profile, children: deleteChild(state.profile.children, payload)}
			};

		default:
			return state;
	}
};

export default profileReducer;
