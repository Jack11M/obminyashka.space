import { getStorageLang } from './index';
import { getTranslatedText } from '../components/local/localisation';

const errorHandling = ( errors, key, isValid, forArray ) => {
	let errorText;
	switch (forArray) {
		case 'phone':
			errorText = '+38(123)456-78-90, 381234567890';
			break;
		case 'children':
			errorText = getTranslatedText( `errors.children`, getStorageLang() );
			break;
		default:
			errorText = getTranslatedText( `errors.regNick`, getStorageLang() );
	}

	const newError = {
		key,
		errorText
	};

	if (isValid) {
		return errors.filter( error => error.key !== key );
	} else {
		return errors.some( error => error.key === key ) ? errors : [ ...errors, newError ];
	}
};

const translateErrors = ( state ) => {
	return state.map( child => {
		const field = Number.isInteger( child.key ) ? 'children' : 'regNick';
		return {
			...child,
			errorText: getTranslatedText( `errors.${ field }`, getStorageLang() )
		};
	} );
};

const permissionToSendProfile = ( state ) => {
	const phones = !state.phones.length ? false : state.phones.every( phone => phone.phoneNumber !== '' );

	if ((state.phones.length > 0 && !phones)) {
		return false;
	}

	const emptyStrings = [ !!state.firstName, !!state.lastName, phones ];
	const errors = [ !!state.errors.length, !!state.errorsPhone.length ];

	return (emptyStrings.some( field => field ) && !errors.some( length => length ));
};

const permissionToSendChildren = ( state ) => {
	if (!state.children.length) {
		return false;
	}
	return state.children[state.children.length - 1].birthDate && !!!state.errorsChildren.length;
};

export { permissionToSendChildren, permissionToSendProfile, translateErrors, errorHandling };
