class ShowErrorsOfInputs {
	static renderErrorStatus(message) {
		const span = document.createElement('span');
		span.className = this.nameClass;
		span.textContent = message;
		const popup = document.querySelector('.popup');
		popup.append(span);
	}

	static clearErrorStatus() {
		const span = document.querySelector(`.${this.nameClass}`);
		if (span) {
			span.remove();
		}
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

	static showErrorRegisterOrLogin(response) {
		if (response.status) {
			for (let status of Object.keys(this.errorsStatus)) {
				if (+status === response.status) {
					for (let message of Object.keys(this.errorsStatus[status])) {
						if (response.message === message) {
							message = this.errorsStatus[status][message];
							this.renderErrorStatus(message)
						}
					}
				}
			}

		} else {
			this.clearErrorStatus();
			console.log('Вы вошли');
			console.log(response.username);
			console.log(response.token);
			return true;
		}
	}

}

ShowErrorsOfInputs.errors = [];
ShowErrorsOfInputs.nameClass = 'error-status';

ShowErrorsOfInputs.errorsStatus = {
	201: {"user registered": "Вы зарегистрированны!"},
	401: {
		"Please enter valid email/login or password":
			"Введите верный логин/email или пароль"
	},
	422: {
		"This login already exists. Please, come up with another login":
			"Этот логин уже существует. Введите другой или" + " войдите в систему ",
		"This email already exists. Please, enter another email or sign in":
			"Этот email существует. Введите другой или войдите" + " в систему "
	}
};