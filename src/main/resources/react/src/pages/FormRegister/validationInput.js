/**
 * @return {boolean}
 */
export const RegisterValidation = (input, isLogin) => {
  // const login = {
  //   usernameOrEmail: [["notEmpty"], ["cutEmpty"]],
  //   password: [["notEmpty"], ["cutEmpty"]]
  // };
  // const register = {
  //   email: [["notEmpty"], ["pattern", "email"], ["cutEmpty"]],
  //   username: [
  //     ["notEmpty"],
  //     ["lengthLogin"],
  //     ["pattern", "altCodeLogin"],
  //     ["cutEmpty"]
  //   ],
  //   password: [
  //     ["pattern", "altCodePassword"],
  //     ["notEmpty"],
  //     ["pattern", "password"],
  //     ["cutEmpty"]
  //   ],
  //   confirmPassword: [["notEmpty"], ["contains", "password"]]
  // };
  //
  // // const validatorMethods = {
  // //   MIN_LENGTH_STRING: 1,
  // //   MAX_LENGTH_STRING: 49,
  // //   rulesPattern: {
  // //     email: /^[A-Z0-9._%+-]{1,129}@[A-Z0-9-]+.+.[A-Z]{2,4}$/i,
  // //     password: /(?=^.{8,30}$)((?=.*\d)|(?=.*\W+))(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).*$/,
  // //     altCodePassword: /^[A-Za-z0-9 «\!»№;%:\?\*()_+\-,@#$^&\[\]\{\}\’\"\.<>`~₴\\|/=]*[A-Za-z0-9][A-Za-z0-9 «\!»№;%:\?\*()_+\-,@#$^&\[\]\{\}\’\"\.<>`~₴\\|/=]*$/,
  // //     altCodeLogin: /^[А-Яа-яёЁЇїІіЄєҐґ0-9A-Za-z0-9 «\!»№;%:\?\*()_+\-,@#$^&\[\]\{\}\’\"\.<>`~₴\\|/=]*[А-Яа-яёЁЇїІіЄєҐґ0-9A-Za-z0-9][А-Яа-яёЁЇїІіЄєҐґ0-9A-Za-z0-9 «\!»№;%:\?\*()_+\-,@#$^&\[\]\{\}\'\"\.<>`~₴\\|/=]*$/
  // //   },
  // //
  // //   notEmpty(el) {
  // //     return el.value !== "";
  // //   },
  // //   cutEmpty(el) {
  // //     return !el.value.includes(" ");
  // //   },
  // //   pattern(el, pattern) {
  // //     return this.rulesPattern[pattern].test(el.value);
  // //   },
  // //   contains(el, el2) {
  // //     const element2 = document.getElementById(el2);
  // //     return el.value === element2.value
  // //   },
  // //   lengthLogin(el) {
  // //     return (
  // //       el.value.length > this.MIN_LENGTH_STRING &&
  // //       el.value.length < this.MAX_LENGTH_STRING
  // //     );
  // //   }
  // // };
  //
  // const whatIsForm = isLogin ? login : register;
  // let methods = whatIsForm[input.name];
  //
  //
  // let someError = methods.map(item => validatorMethods[item[0]](input, item[1]));

  // return someError.every(ev => ev === true);
};
