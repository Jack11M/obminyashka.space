export const addInputMe = (state, item) => {
	const name = Object.getOwnPropertyNames(item)[0];
	const newMe = [...state];
	const idx = newMe.findIndex(item => item.name === name);
	newMe[idx] = {...newMe[idx], value: item[name]};
	return newMe;
}

export const addInputChildren = (state, item, id) => {
	const newState = [...state];
	const name = Object.getOwnPropertyNames(item)[0];
	const idx = newState.findIndex(item => item.id === id);
	const valueInput = {...newState[idx][name], value: item[name]};
	newState[idx] = {...newState[idx], [name]: valueInput};
	return newState;
}

export const deleteChild = (state, id) => {
	return state.filter(item => item.id !== id);
}

export const addChild = (state) => {
	const newState = [...state];
	let newChild = {...newState[newState.length - 1]};
	newChild = {
		...newChild,
		nameChild: {...newChild.nameChild, value: ''},
		ageChild: {...newChild.ageChild, value: ''},
		id: newState[newState.length - 1].id + 1
	}
	return [...newState, newChild];
}