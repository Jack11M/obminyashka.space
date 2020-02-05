function RegisterValidation(data) {
  const formEl = document.getElementById(data.id);
  const formField = formEl.elements;

  let errors = [];
  const rulesPattern = {
    "email-reg": /^\w{1,}@\w{1,}\.\w{2,}$/
  };

  const validatorMethods = {
    minLengthString: 1,
    maxLengthString: 15,

    notEmpty(el) {
      return el.value !== "";
    },
    pattern(el, pattern) {
      return rulesPattern[pattern].test(el.value);
    },
    contains(el, el2) {
      if (el.value === formField[el2].value) {
        formField[el2].nextElementSibling.innerHTML = "";
        return true;
      } else return false;
    },
    notMore(el) {
      return el.value.length < this.maxLengthString;
    },
    lengthLogin(el) {
      return el.value.length > this.minLengthString && !el.value.includes(" ");
    }
  };

  const showError = function(el) {
    el.parentElement.classList.add("error");
    el.style.border = "1px solid red";
    el.nextElementSibling.innerHTML = el.nextElementSibling.dataset.error;
  };
  const showSuccess = function(el) {
    el.parentElement.classList.remove("error");
    el.style.border = "1px solid #bcbcbc";
    el.nextElementSibling.innerHTML = "";
  };

  const isValid = function(el) {
    let methods = data.methods[el.getAttribute("id")];
    if (methods !== undefined) {
      for (let key of methods) {
        if (!validatorMethods[key[0]](el, key[1])) {
          errors.push({ el });
          return false;
        }
      }
    }
    return true;
  };

  const checkIt = function() {
    if (isValid(this)) {
      showSuccess(this);
      for (let i = 0; i < errors.length; i++) {
        if (errors[i].el === this) {
          errors.splice(i, 1);
        }
      }
    } else {
      showError(this);
      let error = errors.find(err => err.el === this);
      if (!error) {
        errors.push({ el: this });
      }
    }
  };

  const checkEmptyInput = function() {
    for (let input of formField) {
      if (input.tagName !== "BUTTON") {
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
    if (val.getAttribute("id") !== "check" && val.tagName !== "BUTTON") {
      val.addEventListener("change", checkIt);
    }
  }

  for (let val of formField) {
    if (val.tagName === "BUTTON") {
      val.addEventListener("click", async function(event) {
        event.preventDefault();
        checkEmptyInput();
        if (errors.length === 0) {
          const connect = new ConnectBase();
          const a = await connect.postRequest(createBody(), val);



        }
      });
    }
  }
}

const validFormRegistration = new RegisterValidation({
  id: "register-form",
  methods: {
    "email-reg": [["notEmpty"], ["pattern", "email-reg"]],
    "login-reg": [["notEmpty"], ["lengthLogin"], ["notMore"]],
    "password-reg": [["notEmpty"], ["notMore"]],
    "confirm-password": [["notEmpty"], ["contains", "password-reg"]]
  }
});

const validFormLogin = new RegisterValidation({
  id: "login-form",
  methods: {
    email: [["notEmpty"]],
    password: [["notEmpty"]]
  }
});
