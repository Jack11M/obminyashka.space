const dataLogin = [
  {
    id: "usernameOrEmail",
    text: "Введите ваш E-mail или логин",
    type: "text",
    dataError: "Введите верный логин или email",
    message: "Please enter valid email/login or password",
  },
  {
    id: "password",
    text: "Введите ваш пароль",
    type: "password",
    dataError: "Неправильный пароль",
  },
];

const dataRegister = [
  {
    id: "email",
    type: "text",
    text: "Введите ваш E-mail",
    dataError: "Укажите действующий адрес электронной почты ",
    message:
      "This email already exists. Please, enter another email or sign in",
  },
  {
    id: "username",
    text: "Введите ваш Логин",
    type: "text",
    dataError: "Больше 2 и меньше 50 символов, без пробелов",
    message: "This login already exists. Please, come up with another login",
  },
  {
    id: "password",
    text: "Введите ваш пароль",
    type: "password",
    dataError: "Латиницей большие, маленькие и цифры от 8 до 30 символов",
  },
  {
    id: "confirmPassword",
    text: "Повторите пароль",
    type: "password",
    dataError: "Пароли должны совпадать",
  },
];

const errorsServer = [
  {
    errorStatus: "Введите верный логин или email.",
    message: "Please enter valid email/login or password",
  },
  {
    errorStatus: "Этот email уже зарегистрирован.",
    message:
      "This email already exists. Please, enter another email or sign in",
  },
  {
    errorStatus: "Этот логин уже зарегистрирован.",
    message: "This login already exists. Please, come up with another login",
  },
];

export { dataRegister, dataLogin, errorsServer };
