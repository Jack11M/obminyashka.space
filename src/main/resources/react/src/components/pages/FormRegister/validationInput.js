/**
 * @return {boolean}
 */
export const RegisterValidation = (input, isLogin) => {
	const login = {
		usernameOrEmail: [["notEmpty"], ["cutEmpty"]],
		password: [["notEmpty"], ["cutEmpty"]]
	};
	const register = {
		email: [["notEmpty"], ["pattern", "email"], ["cutEmpty"]],
		username: [["notEmpty"], ["lengthLogin"], ["cutEmpty"]],
		password: [["notEmpty"], ["pattern", "password"], ["cutEmpty"]],
		confirmPassword: [["notEmpty"], ["contains", "password"]]
	};

	const validatorMethods = {
		MIN_LENGTH_STRING: 1,
		MAX_LENGTH_STRING: 49,
		rulesPattern: {
			email: /.+@.+\..+/i,
			password: /(?=^.{8,30}$)((?=.*\d)|(?=.*\W+))(?=.*[A-Z])(?=.*[a-z]).*$/
		},

		notEmpty(el) {
			return el.value !== "";
		},
		cutEmpty(el) {
			return !el.value.includes(" ");
		},
		pattern(el, pattern) {
			return this.rulesPattern[pattern].test(el.value);
		},
		contains(el, el2) {
			const element2 = document.getElementById(el2);
			return el.value === element2.value && this.notEmpty(el);
		},
		lengthLogin(el) {
			return (
				el.value.length > this.MIN_LENGTH_STRING &&
				el.value.length < this.MAX_LENGTH_STRING &&
				!el.value.includes(" ")
			);
		}
	};

	const whatIsForm = isLogin ? login : register;
	let methods = whatIsForm[input.name];

	let someError = methods.map(item => {
		return validatorMethods[item[0]](input, item[1]);
	});

	return someError.every(ev => ev === true);
};
