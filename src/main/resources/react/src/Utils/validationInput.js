export const validation = ( state, keyInput, value ) => {

	const allVerified = {
		logEmail: [ [ 'notEmpty' ], [ 'cutEmpty' ], [ 'lengthLogin' ] ],
		logPassword: [ [ 'notEmpty' ], [ 'cutEmpty' ] ],
		regEmail: [ [ 'notEmpty' ], [ 'pattern', 'email' ], [ 'cutEmpty' ] ],
		regNick: [
			[ 'notEmpty' ],
			[ 'lengthLogin' ],
			[ 'pattern', 'userName' ],
			[ 'cutEmpty' ]
		],
		regPassword: [
			[ 'notEmpty' ],
			[ 'pattern', 'password' ],
			[ 'cutEmpty' ]
		],
		regConfirm: [ [ 'isEqual', 'regPassword' ] ],
		firstName: [ [ 'pattern', 'name' ] ],
		lastName: [ [ 'pattern', 'name' ] ],
		phoneNumber: [ [ 'pattern', 'phone' ] ],
		oldPassword: [
			[ 'notEmpty' ],
			[ 'pattern', 'password' ],
			[ 'cutEmpty' ]
		],
		newPassword: [
			[ 'pattern', 'password' ],
			[ 'isNotEqual', 'oldPassword' ],
		],
		confirmNewPassword: [ [ 'isEqual', 'newPassword' ] ],
	};

	const verificationMethods = {
		MIN_LENGTH_STRING: 1,
		MAX_LENGTH_STRING: 49,
		rulesPattern: {
			email: /^[A-Z0-9._%+-]{1,129}@[A-Z0-9-]+.+.[A-Z]{2,4}$/i,
			userName: /^(?=.*[A-Za-zА-Яа-я-'`ЁёҐЄІЇієїґ])(?=.*\d*)(?=.[«!»№;%:?*()_+\-,@#$^&[\]{}’".<>`~₴|/=]*)[A-Za-zА-Яа-я-'`ЁёҐЄІЇієїґ\d«!»№;%:?*()_+\-,@#$^&[\]{}’".<>~₴|/=]{2,50}$/,
			password: /^(?=.*[A-Za-z])(?=.*\d)(?=.[«!»№;%:?*()_+\-,@#$^&[\]{}’".<>`~₴|/=]*)[A-Za-z\d«!»№;%:?*()_+\-,@#$^&[\]{}’".<>`~₴|/=]{8,30}$/,
			name: /^$|^[\wА-Яа-я-'`ЁёҐЄІЇієїґ]{2,50}$/,
			phone: /^\s*(?<country>\+?\d{2})[-. (]*(?<area>\d{3})[-. )]*(?<number>\d{3}[-. ]*\d{2}[-. ]*\d{2})\s*$/

		},
		notEmpty( el ) {
			return el !== '';
		},
		cutEmpty( el ) {
			return !el.includes( ' ' );
		},
		pattern( el, pattern ) {
			return this.rulesPattern[pattern].test( el );
		},
		isEqual( el, el2 ) {
			if (el === '') return true;
			return el === state[el2];
		},
		isNotEqual( el, el2 ) {
			if (el === '') return true;
			return el !== state[el2];
		},
		lengthLogin( el ) {
			return (
				el.length > this.MIN_LENGTH_STRING &&
				el.length < this.MAX_LENGTH_STRING
			);
		}
	};

	const methods = allVerified[keyInput];

	const someError = methods.map( item => {
		const [ keyCheck, auxiliary ] = item;
		return verificationMethods[keyCheck]( value, auxiliary );
	} );

	return someError.every( ev => ev === true );
};
