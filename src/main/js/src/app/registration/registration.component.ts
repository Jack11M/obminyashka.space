import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { AuthGuard } from '../auth.guard';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent{
  public messege = ''
  public Status = ''
  public TotalErrorMessege = ''
  public usernameError = false
  public emailError = false
  public passwordError = false
  public confirmPassError = false
  constructor(private _Auth:AuthService, private _router:Router, private _authGuard:AuthGuard) { }

  public Registration(username, email, password, confirmPassword){
    this.usernameError = this._Auth.RegFieldValidation(username, password, email, confirmPassword).username
    this.emailError = this._Auth.RegFieldValidation(username, password, email, confirmPassword).email
    this.passwordError = this._Auth.RegFieldValidation(username, password, email, confirmPassword).password
    this.confirmPassError = this._Auth.RegFieldValidation(username, password, email, confirmPassword).confirmPassword
    this.TotalErrorMessege = this._Auth.RegFieldValidation(username, password, email, confirmPassword).TotalErrorMessege
    if (!this.TotalErrorMessege)
    {
      this._Auth.RegUser(username, email, password, confirmPassword).subscribe(data => {
        this.messege = data.messege
        this.Status = data.Status
      if (data.Status == 201){
        setInterval(() => {
          this.TotalErrorMessege = "Вы зарегестрировались"
        this._authGuard.complitLogin()
        this._router.navigate(['/login'])
      }, 2000)}
      else{
        if (data.message == 'You entered two different passwords. Please try again')
        {
          this.TotalErrorMessege = 'Вы ввели два разных пароля. Пожалуйста, попробуйте еще раз'
        }
        if (data.message == 'Confirm password field is required')
        {
          this.TotalErrorMessege = 'Поле подтвердите пароль  обязательное'
        }
        if (data.message == 'Your password must be between 8 and 30 characters at must contain at least: 1 uppercase Latin character (A-Z), 1 lowercase Latin character (a-z), 1 digit (0-9)')
        {
          this.TotalErrorMessege = 'Ваш пароль должен быть от 8 до 30 символов и обязательно должен включать хотя бы: 1 большую букву латинского алфавита (A-Z), 1 маленькую букву латинского алфавита (a-z), 1 цифру (0-9) '
        }
        if (data.message == 'Password field is required')
        {
          this.TotalErrorMessege = 'Поле password обязательное'
        }
        if (data.message == 'Email field is required')
        {
          this.TotalErrorMessege = 'Поле email обязательное'
        }
        if (data.message == 'This email already exists. Please, enter another email or sign in')
        {
          this.TotalErrorMessege = 'Данный email уже существует. Пожалуйста, введите другой email или войдите в систему'
        }
        if (data.message == 'This login already exists. Please, come up with another login')
        {
          this.TotalErrorMessege = 'Данный логин уже существует. Пожалуйста, придумайте другой'
        }
        if (data.message == 'Your login must be between 2 and 50 characters, do not use space')
        {
          this.TotalErrorMessege = 'Ваш логин должен быть от 2 до 50 символов, не используйте пробел'
        }
        if (data.message == 'Email length should be less than 129 characters. Please enter valid email address')
        {
          this.TotalErrorMessege = 'Длина поля email должна быть не более 129 символов. Пожалуйста, введите существующий email'
        }
        if (data.message == 'Login field is required')
        {
          this.TotalErrorMessege = 'Поле login обязательное'
        }
        if (data.message == 'Please enter valid email address (Ex: username@example.com)')
        {
          this.TotalErrorMessege = 'Пожалуйста, введите валидный email (Например: username@example.com)'
        }
        if (data.message == 'Please enter valid login')
        {
          this.TotalErrorMessege = 'Пожалуйста, введите валидный логин'
        }
      }
      })
    }
  }

}
