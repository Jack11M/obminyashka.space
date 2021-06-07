import { types } from './types';
import {
	addChild,
	addPhone,
	validation,
	changePhoneInputOrChildren,
	dateValidation,
	deleteItem,
	deleteLastPhone,
	deleteReceivedChildren,
	errorHandling,
	genderChange,
	getArray,
	permissionToSendChildren,
	permissionToSendProfile,
	translateErrors
} from '../../Utils';

const initialState = {
	avatarImage: '',
	firstName: '',
	lastName: '',
	username: '',
	email: '',
	phones: [],
	children: [],
	online: '',
	lastOnlineTime: '',
	mockPhones: [
		{
			defaultPhone: false,
			id: 0,
			phoneNumber: ''
		}
	],
	mockChildren: [
		{
			birthDate: '',
			id: 0,
			sex: 'UNSELECTED'
		}
	],
	errors: [],
	errorsPhone: [],
	errorsChildren: [],
	prohibitionToSendAboutMe: true,
	prohibitionToSendChildren: true,
	receivedChildrenFromBack: []
};

const profileReducer = ( state = initialState, { type, payload, id } ) => {
	switch (type) {
		case types.FILL_USER_INFO_SYNC:
			return {
				...state,
				...payload,
				receivedChildrenFromBack: payload.children
			};

		case types.CHANGE_INPUT_VALUE:
			const [ keyInput, value ] = payload;
			const verified = validation( state, keyInput, value );
			const error = errorHandling( state.errors, keyInput, verified );
			return {
				...state, [keyInput]: value, errors: [ ...error ]
			};

		case types.CHANGE_PHONE_INPUT_VALUE:
			const [ keyPhone, valuePhone ] = payload;
			const errorPhone = errorHandling( state.errorsPhone, id, validation( state.phones[id], keyPhone, valuePhone ), 'phone' );
			const phones = getArray( state.phones, state.mockPhones );
			return {
				...state,
				phones: changePhoneInputOrChildren( phones, valuePhone, id, 'phoneNumber' ),
				errorsPhone: errorPhone
			};

		case types.ADD_PHONE:
			return {
				...state,
				phones: addPhone( state.phones, state.errorsPhone )
			};

		case types.DELETE_PHONE:
			return {
				...state,
				phones: deleteLastPhone( state.phones ),
				errorsPhone: deleteLastPhone( state.errorsPhone )
			};

		case types.CHANGE_CHILDREN_INPUT_VALUE: {
			const valid = dateValidation( payload[1] );
			const errorChild = errorHandling( state.errorsChildren, id, valid, 'children' );
			const children = getArray( state.children, state.mockChildren );
			return {
				...state,
				children: changePhoneInputOrChildren( children, payload[1], id, 'birthDate' ),
				errorsChildren: errorChild
			};
		}

		case types.CHOOSE_GENDER: {
			const children = getArray( state.children, state.mockChildren );
			return {
				...state,
				children: genderChange( children, payload, id )
			};
		}

		case types.ADD_CHILD:
			return {
				...state,
				children: addChild( state.children, state.errorsChildren )
			};

		case types.DELETE_CHILD:
			const [ index, childrenId, localDelete ] = payload;

			return {
				...state,
				children: localDelete ? deleteItem( state.children, index ) : deleteReceivedChildren( state.children, childrenId ),
				errorsChildren: deleteItem( state.errorsChildren, index ),
				receivedChildrenFromBack: deleteReceivedChildren( state.receivedChildrenFromBack, childrenId )
			};

		case types.CHANGE_LANG_PROFILE_ERRORS:
			return {
				...state,
				errors: translateErrors( state.errors, payload ),
				errorsChildren: translateErrors( state.errorsChildren, payload )
			};

		case types.CHANGE_PERMISSION_PROFILE:
			return {
				...state,
				prohibitionToSendAboutMe: !permissionToSendProfile( state )
			};

		case types.CHANGE_PERMISSION_CHILDREN:
			return {
				...state,
				prohibitionToSendChildren: !permissionToSendChildren( state )
			};

		case types.PUT_RECEIVED_POST_CHILDREN:
			const childrenWithoutID = [ ...state.children ].filter( item => item.id !== 0 );
			return {
				...state,
				children: [ ...childrenWithoutID, ...payload ],
				receivedChildrenFromBack: [ ...state.receivedChildrenFromBack, ...payload ]
			};

		case types.PUT_RECEIVED_PUT_CHILDREN:
			return {
				...state,
				receivedChildrenFromBack: payload
			};

		default:
			return state;
	}
};

export default profileReducer;
