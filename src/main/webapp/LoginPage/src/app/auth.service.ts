import { Injectable } from '@angular/core';
import { HttpClient} from '@angular/common/http';
import { Observable } from 'rxjs';
import { ILogin, IReg } from 'src/assets/intarface/intarface';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private Login:ILogin = {
    usernameOrEmail: '*',
    password:'*'
  }
  private Registration:IReg = {
    username: '*',
    email: '*',
    password:'*',
    confirmPassword:'*'
  }
  private UsernameErrorMessege = ''
  private EmailErrorMessege = ''
  private PasswordErrorMessege = ''
  private ConfirmPassErrorMessege = ''
  private UsernamevalidationField = false
  private PasswordvalidationField = false
  private EmailvalidationField = false
  private PasswordConfirmvalidationField = false
  constructor(private _http:HttpClient) { }

  public LoginFieldValidation(username, password){
    this.UsernameErrorMessege = ''
    this.PasswordErrorMessege = ''
    this.UsernamevalidationField = false
    this.PasswordvalidationField = false
    if (!username){
      this.UsernamevalidationField = true
      this.UsernameErrorMessege = 'Вы не заполнили это поле!'
    }
    if (!password){
      this.PasswordvalidationField = true
      this.PasswordErrorMessege = 'Вы не заполнили это поле!'
  }
    return {'username': this.UsernamevalidationField, 'password': this.PasswordvalidationField,
            'UsernameMessege':this.UsernameErrorMessege, 'PasswordMessege': this.PasswordErrorMessege}
}

public RegFieldValidation(username, password, email, confirmPassword){
  this.UsernameErrorMessege = ''
  this.EmailErrorMessege = ''
  this.PasswordErrorMessege = ''
  this.ConfirmPassErrorMessege = ''
  this.UsernamevalidationField = false
  this.PasswordvalidationField = false
  this.EmailvalidationField = false
  this.PasswordConfirmvalidationField = false
  if (!username){
    this.UsernamevalidationField = true
    this.UsernameErrorMessege = 'Вы не заполнили это поле!'
  }
  if (!email){
    this.EmailvalidationField = true
    this.EmailErrorMessege = 'Вы не заполнили это поле!'
}
  if (!password){
  this.PasswordvalidationField = true
  this.PasswordErrorMessege = 'Вы не заполнили это поле!'
}
  if (!confirmPassword){
  this.PasswordConfirmvalidationField = true
  this.ConfirmPassErrorMessege = 'Вы не заполнили это поле!'
}
  return {'username': this.UsernamevalidationField, 'password': this.PasswordvalidationField,
          'email': this.EmailvalidationField, 'confirmPassword': this.PasswordConfirmvalidationField,
          'UsernameMessege': this.UsernameErrorMessege, 'PasswordMessege': this.PasswordErrorMessege,
          'EmailMessege': this.EmailErrorMessege, 'confirmPassMessege': this.ConfirmPassErrorMessege}
}

  public LoginUser(username:string, password:string){
    this.Login.usernameOrEmail = username
    this.Login.password = password
    console.log(this.Login);
    console.log(JSON.stringify(this.Login));
    return this._http.post('http://54.37.125.180:8080/auth/login', JSON.stringify(this.Login))
  }
  public RegUser(username:string, email:string, password:string, confirmPassword:string){
      this.Login.usernameOrEmail = username
      this.Login.password = password
      return this._http.post('http://54.37.125.180:8080/auth/register', JSON.stringify(this.Registration))
  }
}
