const PHONE_MAX = 5;
const CHILDREN_MAX = 10;
const YEARS_AGO = 17;
const date = new Date();

export const getCurrentDate = () => {
	return date.toISOString().substr( 0, 10 );
};

export const getMinDate = () => {
	let year = getCurrentDate().slice( 0, 4 ) - YEARS_AGO;
	let rest = getCurrentDate().slice( 4 );
	return `${ year }${ rest }`;
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

export const changePhoneInputOrChildren = ( state, value, id, property ) => {
	return state.map( ( item, idx ) => {
		if (idx === id) {
			return { ...item, [property]: value };
		}
		return item;
	} );
};

export const addPhone = ( data, errors ) => {
	if (!data.length || data.length >= PHONE_MAX || data[data.length - 1].phoneNumber === '' || errors.length) {
		return data;
	}
	return [ ...data, { defaultPhone: false, id: 0, phoneNumber: '' } ];
};

export const putChildren = ( state, child ) => {
	const arrayChildren = [ ...state ];
	arrayChildren[arrayChildren.length - 1] = child;
	return arrayChildren;
};

export const genderChange = ( state, sex, id ) => {
	return state.map( ( child, idx ) => {
		if (idx === id) {
			return { ...child, sex };
		} else {
			return child;
		}
	} );
};

export const deleteLastPhone = ( data ) => {
	let newData = [ ...data ];
	newData.pop();
	return newData;
};

export const addChild = ( state, errors ) => {
	if (!state.length || state.length >= CHILDREN_MAX || state[state.length - 1].birthDate === '' || errors.length) {
		return state;
	}
	const newChild = {
		id: 0,
		birthDate: '',
		sex: 'UNSELECTED'
	};
	return [ ...state, newChild ];
};

export const deleteItem = ( state, id ) => {
	return state.filter( ( item, idx ) => idx !== id );
};
export const deleteReceivedChildren = ( state, id ) => {
	return state.filter( ( item ) => item.id !== id );
};

export const dateValidation = ( date ) => {
	const chooseDate = Date.parse( date );
	const currentDate = Date.parse( getCurrentDate() );
	const lastDate = Date.parse( getMinDate() );
	return chooseDate >= lastDate && chooseDate <= currentDate;
};

export const getArray = ( state, mock ) => {
	return !state.length ? mock : state;
};
