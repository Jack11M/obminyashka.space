(function changeList() {
  let arrayList = document.querySelectorAll('.tabs li');
  let login = document.querySelector('.login');
  let register = document.querySelector('.register');
  let arrayId = [login, register];

  for (let key of arrayList) {
    key.addEventListener('click', function() {
      if (this.className !== 'active') {
        toggle();
        new IsEmptyInputs().clearInputs();
      }
    });
  }

  function toggle() {
    for (let key of arrayList) {
      if (key.className !== 'active') {
        key.className = 'active';
      } else {
        key.classList.remove('active');
      }
    }

    for (let key of arrayId) {
      key.classList.toggle('switch');
    }
  }
})();
