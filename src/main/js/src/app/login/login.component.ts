import { Component} from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { AuthGuard } from '../auth.guard';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent{
  public messege = ''
  public Status = ''
  public TotalErrorMessege = ''
  public usernameError = false
  public passwordError = false

  constructor(private _Auth:AuthService, private _router:Router, private _authGuard:AuthGuard){}

  public login(username, password){
    this.usernameError = this._Auth.LoginFieldValidation(username, password).username
    this.passwordError = this._Auth.LoginFieldValidation(username, password).password
    this.TotalErrorMessege = this._Auth.LoginFieldValidation(username, password).TotalErrorMessege
    if (!this.TotalErrorMessege)
    {
      this._Auth.LoginUser(username, password).subscribe(data => {
        this.messege = data.messege
        this.Status = data.Status
        if (data.Status == 200){
          this._authGuard.complitLogin()
          this._router.navigate(['/home'])
        }
        else{
          if (data.messege = 'Please enter valid email/login or password'){
            this.TotalErrorMessege = 'Пожалуйста введите валидный Email/login или пароль!'
          }
        }
      })
    }
  }
}
