class ShowErrorsOfInputs {
  static isErrorStatus(message, data) {
    if (
      message === "Введите верный логин или email." ||
      message === "Этот email уже существует."
    ) {
      this.showErrorStatus(data[0], message);
    }
    if (message === "Этот логин уже существует.") {
      this.showErrorStatus(data[1], message);
    }
  }

  static showErrorStatus(el, message) {
    el.parentElement.classList.add("error");
    el.style.border = "1px solid red";
    el.nextElementSibling.innerHTML = message;
  }

  static showError(el) {
    el.parentElement.classList.add("error");
    el.style.border = "1px solid red";
    el.nextElementSibling.innerHTML = el.nextElementSibling.dataset.error;
  }

  static showSuccess(el) {
    el.parentElement.classList.remove("error");
    el.style.border = "1px solid #bcbcbc";
    el.nextElementSibling.innerHTML = "";
  }

  static showErrorRegisterOrLogin(response, data) {
    if (response.status) {
      for (let status of Object.keys(this.errorsStatus)) {
        if (+status === response.status) {
          for (let message of Object.keys(this.errorsStatus[status])) {
            if (response.message === message) {
              message = this.errorsStatus[status][message];
              this.isErrorStatus(message, data);
            }
          }
        }
      }
    } else {
      this.clearErrorStatus();
      console.log("Вы вошли");
      console.log(response.username);
      console.log(response.token);
      return true;
    }
  }
}

ShowErrorsOfInputs.errors = [];
ShowErrorsOfInputs.errorsStatus = {
  201: { "user registered": "Вы зарегистрированны!" },
  401: {
    "Please enter valid email/login or password":
      "Введите верный логин или email."
  },
  422: {
    "This login already exists. Please, come up with another login":
      "Этот логин уже существует.",
    "This email already exists. Please, enter another email or sign in":
      "Этот email уже существует."
  }
};
