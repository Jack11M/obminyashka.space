import { Component} from '@angular/core';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent{
  public UsernameErrorMessege = ''
  public PasswordErrorMessege = ''
  public EmailErrorMessege = ''
  public PasswordConfirmErrorMessege = ''
  public usernameError = false
  public emailError = false
  public passwordError = false
  public confirmPassError = false
  constructor(private _Auth:AuthService) { }

  public Registration(username, email, password, confirmPassword){
    this.usernameError = this._Auth.RegFieldValidation(username, password, email, confirmPassword).username
    this.emailError = this._Auth.RegFieldValidation(username, password, email, confirmPassword).email
    this.passwordError = this._Auth.RegFieldValidation(username, password, email, confirmPassword).password
    this.confirmPassError = this._Auth.RegFieldValidation(username, password, email, confirmPassword).confirmPassword
    this.UsernameErrorMessege = this._Auth.RegFieldValidation(username, password, email, confirmPassword).UsernameMessege
    this.PasswordErrorMessege = this._Auth.RegFieldValidation(username, password, email, confirmPassword).PasswordMessege
    this.EmailErrorMessege = this._Auth.RegFieldValidation(username, password, email, confirmPassword).EmailMessege
    this.PasswordConfirmErrorMessege = this._Auth.RegFieldValidation(username, password, email, confirmPassword).confirmPassMessege
    if (!this.UsernameErrorMessege && !this.EmailErrorMessege && !this.PasswordErrorMessege && !this.PasswordConfirmErrorMessege)
    {
      this._Auth.RegUser(username, email, password, confirmPassword).subscribe(data => {console.log(data)})
    }
  }

}
