export const addInputPhoneOrChildren = ( state, input, id ) => {
	return state.map( item => {
		if (item.id === id) {
			return { ...item, ...input };
		}
		return item;
	} );
};

export const deleteChild = ( state, id ) => {
	return state.filter( item => item.id !== id );
};

export const addChild = ( state ) => {
	if (!state[state.length - 1].birthDate ||!state[state.length - 1].sex || state.length >= 10) {
		return state;
	}
	let newChild = {
		'birthDate': '',
		'sex': '',
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