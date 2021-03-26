import { types } from './types';
import { addChild, addInputPhoneOrChildren, addPhone, deleteItem, deleteLastPhone, fillUserInfo } from '../Utils';


const initialState = {
	profile: {
		avatarImage: '',
		firstName: '',
		lastName: '',
		username: '',
		email: '',
		phones: [
			{
				defaultPhone: false,
				id: 0,
				phoneNumber: '',
				show: true
			}
		],
		children: [
			{
				name: '',
				birthDate: '',
				id: 0,
				sex: 'boy'
			}
		],
		online: '',
		lastOnlineTime: ''
	},
	profileFromServer: {},
	isOpen: false,
	modalIsOpening: 0,
};

const profileReducer = ( state = initialState, { type, payload, id} ) => {
	switch (type) {
		case types.FILL_USER_INFO_SYNC:
			return {
				...state,
				profile: { ...state.profile, ...fillUserInfo( payload ) }
			};

		case types.ADD_ME_INPUT_VALUE:
			return {
				...state,
				profile: { ...state.profile, ...payload }
			};

		case types.ADD_PHONE_INPUT_VALUE:
			return {
				...state,
				profile: { ...state.profile, phones: addInputPhoneOrChildren( state.profile.phones, payload, id ) }
			};

		case types.ADD_PHONE:
			return {
				...state,
				profile: { ...state.profile, phones: addPhone( state.profile.phones ) }
			};

		case types.DELETE_PHONE:
			return {
				...state,
				profile: { ...state.profile, phones: deleteLastPhone( state.profile.phones ) }
			};

		case types.ADD_CHILDREN_INPUT_VALUE: {
			return {
				...state,
				profile: { ...state.profile, children: addInputPhoneOrChildren( state.profile.children, payload, id ) }
			};
		}

		case types.ADD_CHILD:
			return {
				...state,
				profile: { ...state.profile, children: addChild( state.profile.children ) }
			};

		case types.DELETE_CHILD:
			return {
				...state,
				profile: { ...state.profile, children: deleteItem( state.profile.children, payload ) }
			};

		case types.OPEN_EXIT_MODAL:
			return {
				...state,
				isOpen: !state.isOpen,
			};

		case types.CHECK_FIRST_OPENING:
			return {
				...state,
				modalIsOpening: state.modalIsOpening,
			}

		default:
			return state;
	}
};

export default profileReducer;
