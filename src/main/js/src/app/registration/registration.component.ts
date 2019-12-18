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
    this._Auth.RegUser(username, email, password, confirmPassword).subscribe(data => {console.log(data)})
      })
    }
  }

}
