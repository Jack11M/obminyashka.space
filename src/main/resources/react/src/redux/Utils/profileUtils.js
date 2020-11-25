const CHILDREN_MAX = 10;
const PHONE_MAX = 5;

export const addInputPhoneOrChildren = ( state, input, id ) => {
	return state.map( item => {
		if (item.id === id) {
			return { ...item, ...input };
		}
		return item;
	} );
};

export const deleteItem = ( state, id ) => {
	return state.filter( item => item.id !== id );
};

export const addChild = ( state ) => {
	if (state.length >= CHILDREN_MAX || !state[state.length - 1].birthDate || !state[state.length - 1].name ) {
		return state;
	}
	const newChild = {
		name: '',
		birthDate: '',
		sex: 'boy',
		id: state[state.length - 1].id + 1
	};
	return [ ...state, newChild ];
};

export const fillUserInfo = ( data ) => {
	const { phones, children, ...rest } = data;
	if (!phones.length && !children.length) {
		return rest;
	}
	if (!phones.length) {
		return { ...children, ...rest };
	}
};

export const addPhone = ( data ) => {
	if (data.length >= PHONE_MAX || data[data.length - 1].phoneNumber === '' ) {
		return data;
	}
	const newPhone = {
		defaultPhone: false,
		id: data[data.length - 1].id + 1,
		phoneNumber: '',
		show: true
	};
	return [ ...data, newPhone ];
};

export const deleteLastPhone = ( data ) => {
	let newData = [...data]
	newData.pop()
	return newData
};