class ConnectToBase {
  constructor() {
    this._url = 'http://54.37.125.180:8080/auth/';
    this.headers = { 'Content-Type': 'application/json' };
  }

  async postRequest(body, value) {
    if (value.classList[1] === 'btn-login') {
      this._url += 'login';
    } else {
      this._url += 'register';
    }
    const response = await fetch(this._url, {
      method: 'POST',
      headers: this.headers,
      body: JSON.stringify(body)
    }).catch(e => {
      console.log(e);
      alert('Нет подключения к базе');
    });
    try {
      if (response) {
        return await response.json();
      }
    } catch (e) {
      console.log(e);
      alert('Что-то пошло не так...');
    }
  }
}
