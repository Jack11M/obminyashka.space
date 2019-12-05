import { Injectable } from '@angular/core';
import { HttpClient} from '@angular/common/http';
import { ILogin, IReg } from 'src/assets/intarface/intarface';

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
  private UsernameErrorMessage = ''
  private EmailErrorMessage = ''
  private PasswordErrorMessage = ''
  private ConfirmPassErrorMessage = ''
  private UsernameValidationField = false
  private PasswordValidationField = false
  private EmailValidationField = false
  private PasswordConfirmValidationField = false
  constructor(private _http:HttpClient) { }

  public LoginFieldValidation(username, password){
    this.UsernameErrorMessage = ''
    this.PasswordErrorMessage = ''
    this.UsernameValidationField = false
    this.PasswordValidationField = false
    if (!username){
      this.UsernameValidationField = true
      this.UsernameErrorMessage = 'Вы не заполнили это поле!'
    }
    if (!password){
      this.PasswordValidationField = true
      this.PasswordErrorMessage = 'Вы не заполнили это поле!'
  }
    return {'username': this.UsernameValidationField, 'password': this.PasswordValidationField,
            'UsernameMessege':this.UsernameErrorMessage, 'PasswordMessege': this.PasswordErrorMessage}
}

public RegFieldValidation(username, password, email, confirmPassword){
  this.UsernameErrorMessage = ''
  this.EmailErrorMessage = ''
  this.PasswordErrorMessage = ''
  this.ConfirmPassErrorMessage = ''
  this.UsernameValidationField = false
  this.PasswordValidationField = false
  this.EmailValidationField = false
  this.PasswordConfirmValidationField = false
  if (!username){
    this.UsernameValidationField = true
    this.UsernameErrorMessage = 'Вы не заполнили это поле!'
  }
  if (!email){
    this.EmailValidationField = true
    this.EmailErrorMessage = 'Вы не заполнили это поле!'
}
  if (!password){
  this.PasswordValidationField = true
  this.PasswordErrorMessage = 'Вы не заполнили это поле!'
}
  if (!confirmPassword){
  this.PasswordConfirmValidationField = true
  this.ConfirmPassErrorMessage = 'Вы не заполнили это поле!'
}
if (password == confirmPassword){
  if (email.includes("@") && email.includes(".")){
  }
  else{
    this.EmailErrorMessage = 'Вы ввели не верный Email!'
    this.EmailValidationField = true
  }
}
else{
  this.PasswordErrorMessage = 'Ваши пароли не совподают!'
  this.ConfirmPassErrorMessage = 'Ваши пароли не совподают!'
  this.PasswordValidationField = true
  this.PasswordConfirmValidationField = true
}
  return {'username': this.UsernameValidationField, 'password': this.PasswordValidationField,
          'email': this.EmailValidationField, 'confirmPassword': this.PasswordConfirmValidationField,
          'UsernameMessege': this.UsernameErrorMessage, 'PasswordMessege': this.PasswordErrorMessage,
          'EmailMessege': this.EmailErrorMessage, 'confirmPassMessege': this.ConfirmPassErrorMessage}
}

  public LoginUser(username:string, password:string){
    this.Login.usernameOrEmail = username
    this.Login.password = password
    return this._http.post('http://localhost:8080/auth/login', this.Login)
  }
  public RegUser(username:string, email:string, password:string, confirmPassword:string){
      this.Registration.username = username
      this.Registration.password = password
      this.Registration.email = email
      this.Registration.confirmPassword = confirmPassword
      return this._http.post('http://localhost:8080/auth/register', this.Registration)
  }
}
