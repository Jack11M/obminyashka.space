export const addMeInputValue = (data) => ({
	type: "ADD_ME_INPUT_VALUE",
	payload: data
});

export const addChildrenInputValue = (data, id) => ({
	type: "ADD_CHILDREN_INPUT_VALUE",
	payload: data,
	id
});

export const deleteChild = (id) => ({
	type: "DELETE_CHILD",
	payload: id
});

export const addChild = () => ({
	type: "ADD_CHILD"
});