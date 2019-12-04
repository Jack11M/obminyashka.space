import { Component} from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent{
  public UsernameErrorMessege = ''
  public PasswordErrorMessege = ''
  public usernameError = false
  public passwordError = false

  constructor(private _Auth:AuthService){}

  public login(username, password){
      this._Auth.LoginUser(username, password).subscribe(data => {console.log(data)})
      this.usernameError = this._Auth.LoginFieldValidation(username, password).username
      this.passwordError = this._Auth.LoginFieldValidation(username, password).password
      this.UsernameErrorMessege = this._Auth.LoginFieldValidation(username, password).UsernameMessege
      this.PasswordErrorMessege = this._Auth.LoginFieldValidation(username, password).PasswordMessege
  }
}
