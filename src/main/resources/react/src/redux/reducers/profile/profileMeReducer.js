import { types } from '../../types'
import { addChild, addInputChildren, addInputMe, deleteChild } from '../../Utils/profileUtils';

const dataInput = [
	{name: "firstName", label: "Имя", value: ""},
	{name: "lastName", label: "Фамилия", value: ""},
	{name: "city", label: "Город", value: ""},
	{name: "phone", label: "Телефон", value: ""}
]

const initialState = {
		me: [
			{name: "firstName", label: "Имя", value: ""},
			{name: "lastName", label: "Фамилия", value: ""},
			{name: "city", label: "Город", value: ""},
			{name: "phone", label: "Телефон", value: ""}
		],
		children: [
			{
				id: 0,
				nameChild: {name: `nameChild`, label: "Имя", value: ""},
				ageChild: {name: `ageChild`, label: "Возраст", value: ""}
			}
		]
	}
;

const profileMeReducer = (state = initialState, {type, payload, id}) => {
	switch (type) {
		case types.ADD_ME_INPUT_VALUE: {
			return {
				...state,
				me: addInputMe(state.me, payload)
			}
		}
		case types.ADD_CHILDREN_INPUT_VALUE: {
			return {
				...state,
				children: addInputChildren(state.children, payload, id)
			}
		}
		case types.ADD_CHILD:
			return {
				...state,
				children: addChild(state.children)
			}
		case types.DELETE_CHILD:
			return {
				...state,
				children: deleteChild(state.children, payload)
			}
		default:
			return state;
	}
};

export default profileMeReducer;
