import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent{

  constructor(private _Auth:AuthService) { }

  public Registration(username, email, password, confirmPassword){
    console.log(this._Auth.RegUser(username, email, password, confirmPassword))
  }

}
