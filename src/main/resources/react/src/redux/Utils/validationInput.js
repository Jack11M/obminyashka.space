export const authValidation = ( state, keyInput, value ) => {

	const allVerified = {
		logEmail: [ [ 'notEmpty' ], [ 'cutEmpty' ], [ 'lengthLogin' ] ],
		logPassword: [ [ 'notEmpty' ], [ 'cutEmpty' ] ],
		regEmail: [ [ 'notEmpty' ], [ 'pattern', 'email' ], [ 'cutEmpty' ] ],
		regNick: [
			[ 'notEmpty' ],
			[ 'lengthLogin' ],
			[ 'pattern', 'altCodeLogin' ],
			[ 'cutEmpty' ]
		],
		regPassword: [
			[ 'pattern', 'altCodePassword' ],
			[ 'notEmpty' ],
			[ 'pattern', 'password' ],
			[ 'cutEmpty' ]
		],
		regConfirm: [ [ 'contains', 'regPassword' ] ],
		firstName: [ [ 'pattern', 'firstname' ] ],
		lastName: [ [ 'pattern', 'lastname' ] ],
		phoneNumber: [ [ 'pattern', 'phone' ] ],
	};

	const verificationMethods = {
		MIN_LENGTH_STRING: 1,
		MAX_LENGTH_STRING: 49,
		rulesPattern: {
			firstname: /^$|^[\wА-Яа-я-'`ЁёҐЄІЇієїґ]{2,50}$/,
			lastname: /^$|^[\wА-Яа-я-'`ЁёҐЄІЇієїґ]{2,50}$/,
			phone: /^\s*(?<country>\+?\d{2})[-. (]*(?<area>\d{3})[-. )]*(?<number>\d{3}[-. ]*\d{2}[-. ]*\d{2})\s*$/,

			email: /^[A-Z0-9._%+-]{1,129}@[A-Z0-9-]+.+.[A-Z]{2,4}$/i,
			password: /(?=^.{8,30}$)((?=.*\d)|(?=.*\W+))(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).*$/,
			altCodeLogin: /^[А-Яа-яёЁЇїІіЄєҐґ0-9A-Za-z0-9 «\!»№;%:\?\*()_+\-,@#$^&\[\]\{\}\’\"\.<>`~₴\\|/=]*[А-Яа-яёЁЇїІіЄєҐґ0-9A-Za-z0-9][А-Яа-яёЁЇїІіЄєҐґ0-9A-Za-z0-9 «\!»№;%:\?\*()_+\-,@#$^&\[\]\{\}\'\"\.<>`~₴\\|/=]*$/,
			altCodePassword: /^[A-Za-z0-9 «\!»№;%:\?\*()_+\-,@#$^&\[\]\{\}\’\"\.<>`~₴\\|/=]*[A-Za-z0-9][A-Za-z0-9 «\!»№;%:\?\*()_+\-,@#$^&\[\]\{\}\’\"\.<>`~₴\\|/=]*$/
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
		contains( el, el2 ) {
			if (el === '') return true;
			return el === state[el2].value;
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
