import { Injectable } from '@angular/core';
import { HttpClient} from '@angular/common/http';
import { Observable } from 'rxjs';
import { ILogin, IReg } from 'src/assets/intarface/intarface';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  public Login:ILogin = {
    usernameOrEmail: '*',
    password:'*'
  }
  public Registration:IReg = {
    username: '*',
    email: '*',
    password:'*',
    confirmPassword:'*'
  }

  constructor(private _http:HttpClient) { }


  public LoginUser(username:string, password:string){
    if (!username || !password){
      alert('Извините но вы должны заполнить поля!')
      return
    }
    this.Login.usernameOrEmail = username
    this.Login.password = password
    console.log(this.Login);
    console.log(JSON.stringify(this.Login));
    this._http.post('http://54.37.125.180:8080/auth/login', JSON.stringify(this.Login))
  }
  public RegUser(username:string, email:string, password:string, confirmPassword:string){
    if (!username || !password || !email || !confirmPassword){
      alert('Извините но вы должны заполнить поля!')
    }
    if (password != confirmPassword){
      alert('Ваш пароль не соответствует паролю!')
    }
    else{
      this.Login.usernameOrEmail = username
      this.Login.password = password
      this._http.post('http://54.37.125.180:8080/auth/register', JSON.stringify(this.Registration))
    }
  }
}
