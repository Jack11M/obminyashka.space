class ConnectBase {
  constructor() {
    this._url = "http://localhost:3000/auth/";
    this.headers = {
      "Content-Type": "application/json"
    };
  }

  async postRequest(body, value) {
    let url;
    if (value.classList[1] === "btn-login") {
      url = this._url + "login";
    } else {
      url = this._url + "register";
      document.querySelector(".auth").style.display = "none";
    }
    return await fetch(url, {
      method: "POST",
      headers: this.headers,
      body: JSON.stringify(body)
    }).then(data => data.json());
  }
}
