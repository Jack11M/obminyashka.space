import { getStorageLang } from './index';
import { getTranslatedText } from '../../components/local/localisation';

export const errorHandling = ( errors, key, verified, forArray ) => {
	const lang = getStorageLang();
	let errorText;

	switch (forArray) {
		case 'phone':
			errorText = '+38(123)456-78-90, 381234567890';
			break;
		case 'children':
			errorText = getTranslatedText( `errors.children`, lang );
			break;
		default:
			errorText = getTranslatedText( `errors.regNick`, lang );
	}

	const newError = {
		key,
		errorText
	};

	if (verified) {
		return errors.filter( error => error.key !== key );
	} else {
		return errors.some( error => error.key === key ) ? errors : [ ...errors, newError ];
	}
};

export const translateErrors = ( state, lang ) => {
	return state.map( child => {
		const field = Number.isInteger( child.key ) ? 'children' : 'regNick';
		return {
			...child,
			errorText: getTranslatedText( `errors.${ field }`, lang )
		};
	} );
};

export const permissionToSendProfile = ( state ) => {
	const phones = !state.phones.length ? false : state.phones.every( phone => phone.phoneNumber !== '' );

	if ((state.phones.length > 0 && !phones)) {
		return false;
	}

	const emptyStrings = [ !!state.firstName, !!state.lastName, phones ];
	const errors = [ !!state.errors.length, !!state.errorsPhone.length ];

	return (emptyStrings.some( field => field ) && !errors.some( length => length ));
};

export const permissionToSendChildren = ( state ) => {
	if (!state.children.length) {
		return false;
	}
	return state.children[state.children.length - 1].birthDate && !!!state.errorsChildren.length;
};
