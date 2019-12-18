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
  private TotalErrorMessege = ''
  private UsernamevalidationField = false
  private PasswordvalidationField = false
  private EmailvalidationField = false
  private PasswordConfirmvalidationField = false
  public CurentUsername = ''
  constructor(private _http:HttpClient) { }

  public LoginFieldValidation(username, password){
    this.TotalErrorMessege = ''
    this.UsernamevalidationField = false
    this.PasswordvalidationField = false
    if (!username){
      this.UsernamevalidationField = true
      this.TotalErrorMessege = 'Вы заполнили не все поля!'
    }
    if (!password){
      this.PasswordvalidationField = true
      this.TotalErrorMessege = 'Вы заполнили не все поля!'
    }
    return {'username': this.UsernamevalidationField, 'password': this.PasswordvalidationField,
            'TotalErrorMessege':this.TotalErrorMessege,}
}

public RegFieldValidation(username, password, email, confirmPassword){
  this.TotalErrorMessege = ''
  this.UsernamevalidationField = false
  this.PasswordvalidationField = false
  this.EmailvalidationField = false
  this.PasswordConfirmvalidationField = false
  if (password != confirmPassword){
    this.TotalErrorMessege = 'Ваши пароли не совподают!'
    this.PasswordvalidationField = true
    this.PasswordConfirmvalidationField = true
  }
  if (!email.includes("@") && !email.includes(".")){
    this.TotalErrorMessege = 'Вы ввели не верный Email!'
    this.EmailvalidationField = true
    }
  if (!username){
    this.UsernamevalidationField = true
    this.TotalErrorMessege ='Вы заполнили не все поля!'
  }
  if (!email){
    this.EmailvalidationField = true
    this.TotalErrorMessege = 'Вы заполнили не все поля!'
}
  if (!password){
  this.PasswordvalidationField = true
  this.TotalErrorMessege = 'Вы заполнили не все поля!'
}
  if (!confirmPassword){
  this.PasswordConfirmvalidationField = true
  this.TotalErrorMessege = 'Вы заполнили не все поля'
}
  return {'username': this.UsernamevalidationField, 'password': this.PasswordvalidationField,
          'email': this.EmailvalidationField, 'confirmPassword': this.PasswordConfirmvalidationField,
          'TotalErrorMessege': this.TotalErrorMessege}
}

  public LoginUser(username:string, password:string){
    this.Login.usernameOrEmail = username
    this.Login.password = password
    return this._http.post('http://54.37.125.180:8080/auth/login', this.Login)
  }
  public RegUser(username:string, email:string, password:string, confirmPassword:string){
      this.Registration.username = username
      this.Registration.password = password
      this.Registration.email = email
      this.Registration.confirmPassword = confirmPassword
      return this._http.post('http://54.37.125.180:8080/auth/register', this.Registration)
  }
}
