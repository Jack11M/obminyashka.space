class IsEmptyInputs {
  constructor() {
    this.emptyInput = document.querySelectorAll('.auth input');
  }

  clearInputs() {
    ShowErrorsOfInputs.errors = [];
    this.emptyInput.forEach(input => {
      if (input.id !== 'check' && input.id !== 'check-reg') {
        input.value = '';
        ShowErrorsOfInputs.showSuccess(input);
      }
    });

  }
}
