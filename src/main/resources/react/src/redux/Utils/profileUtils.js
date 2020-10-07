export const addInputPhoneOrChildren = (state, input, id) => {
	return state.map(item => {
		if (item.id === id) {
			return {...item, ...input};
		}
		return item;
	});
};

export const deleteChild = (state, id) => {
	return state.filter(item => item.id !== id);
};

export const addChild = (state) => {
	let newChild = {...state[state.length - 1]};
	newChild = {...newChild, id: state[state.length - 1].id + 1};
	return [...state, newChild];
};