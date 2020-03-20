/**
 * @return {boolean}
 */
export const RegisterValidation = (input, isLogin) => {
  const login = {
    usernameOrEmail: [["notEmpty"]],
    password: [["notEmpty"]]
  };
  const register = {
    email: [["notEmpty"], ["pattern", "email"]],
    username: [["notEmpty"], ["lengthLogin"]],
    password: [["notEmpty"], ["pattern", "password"]],
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

  return (() => {
    const whatIsForm = isLogin ? login : register;
    let methods = whatIsForm[input.name];
    let isLogic = false;

    methods.forEach(item => {
      isLogic = validatorMethods[item[0]](input, item[1]);
    });
    return isLogic;
  })();
};
