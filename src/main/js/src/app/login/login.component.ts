import {Component} from '@angular/core';
import {AuthService} from '../auth.service';
import {Router} from '@angular/router';
import {AuthGuard} from '../auth.guard';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  public messege = ''
  public Status = ''
  public TotalErrorMessege = ''
  public usernameError = false
  public passwordError = false

  constructor(private _Auth: AuthService, private _router: Router, private _authGuard: AuthGuard) {
  }

  public login(username, password) {
    this.usernameError = this._Auth.LoginFieldValidation(username, password).username
    this.passwordError = this._Auth.LoginFieldValidation(username, password).password
    this.TotalErrorMessege = this._Auth.LoginFieldValidation(username, password).TotalErrorMessege
    this._Auth.LoginUser(username, password).subscribe(data => {
      console.log(data)
    })
  }
}
