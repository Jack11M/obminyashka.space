const RegisterValidation = function (data) {
  const formEl = document.getElementById(data.id);
  const formField = formEl.elements;

  const validatorMethods = {
    MIN_LENGTH_STRING: 1,
    MAX_LENGTH_STRING: 49,
    rulesPattern: {
      'email-reg': /.+@.+\..+/i,
      'password-reg': /(?=^.{8,30}$)((?=.*\d)|(?=.*\W+))(?=.*[A-Z])(?=.*[a-z]).*$/
    },

    notEmpty(el) {
      return el.value !== '';
    },
    pattern(el, pattern) {
      return this.rulesPattern[pattern].test(el.value);
    },
    contains(el, el2) {
      if (el.value === formField[el2].value) {
        formField[el2].nextElementSibling.innerHTML = '';
        return true;
      }
      return false;
    },
    lengthLogin(el) {
      return (
        el.value.length > this.MIN_LENGTH_STRING &&
        el.value.length < this.MAX_LENGTH_STRING &&
        !el.value.includes(' ')
      );
    }
  };

  const isValid = function(el) {
    let methods = data.methods[el.getAttribute('id')];
    if (methods !== undefined) {
      for (let key of methods) {
        if (!validatorMethods[key[0]](el, key[1])) {
          ShowErrorsOfInputs.errors.push({ el });
          return false;
        }
      }
    }
    return true;
  };

  const checkIt = function() {
    if (isValid(this)) {
      ShowErrorsOfInputs.showSuccess(this);
      for (let i = 0; i < ShowErrorsOfInputs.errors.length; i++) {
        if (ShowErrorsOfInputs.errors[i].el === this) {
          ShowErrorsOfInputs.errors.splice(i, 1);
        }
      }
    } else {
      ShowErrorsOfInputs.showError(this);
      let error = ShowErrorsOfInputs.errors.find(err => err.el === this);
      if (!error) {
        ShowErrorsOfInputs.errors.push({ el: this });
      }
    }
  };

  const checkEmptyInput = function() {
    for (let input of formField) {
      if (input.tagName !== 'BUTTON' && input.id !== 'check' && input.id !== 'check-reg') {
        checkIt.call(input);
      }
    }
  };

  const createBody = function() {
    const formData = new FormData(formEl);
    const body = {};
    for (let key of formData) {
      const [name, value] = key;
      body[name] = value;
    }
    return body;
  };

  for (let val of formField) {
    if (val.id !== 'check' && val.tagName !== 'BUTTON' && val.id !== 'check-reg') {
      val.addEventListener('change', checkIt);
    }
  }

  for (let val of formField) {
    if (val.tagName === 'BUTTON') {
      val.addEventListener('click', async function(event) {
        event.preventDefault();
        checkEmptyInput();
        // const checkedInput = document.getElementById('check-reg');
        if (ShowErrorsOfInputs.errors.length === 0) {
          const connect = new ConnectToBase();
          const response = await connect.postRequest(createBody(), val);

          ShowErrorsOfInputs.showErrorRegisterOrLogin(response, formField);
          console.log(response);
        }
      });
    }
  }
};

const validFormRegistration = new RegisterValidation({
  id: 'register-form',
  methods: {
    'email-reg': [['notEmpty'], ['pattern', 'email-reg']],
    'login-reg': [['notEmpty'], ['lengthLogin']],
    'password-reg': [['notEmpty'], ['pattern', 'password-reg']],
    'confirm-password': [['notEmpty'], ['contains', 'password-reg']]
  }
});

const validFormLogin = new RegisterValidation({
  id: 'login-form',
  methods: {
    email: [['notEmpty']],
    password: [['notEmpty']]
  }
});
