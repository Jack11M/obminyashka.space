class ConnectBase {
  constructor() {
    this._url = "http://localhost:3000/auth/";
    this.headers = {
      "Content-Type": "application/json"
    };
  }

  async postRequest(body, value) {
    if (value.classList[1] === "btn-login") {
      this._url += "login";
    } else {
      this._url += "register";
    }
    const response = await fetch(this._url, {
      method: "POST",
      headers: this.headers,
      body: JSON.stringify(body)
    });
    try {
      if (response.ok) {
        return await response.json();
      }
    } catch (e) {
      console.log(e);
      alert("Что-то пошло не так...");
    }

  }
}
