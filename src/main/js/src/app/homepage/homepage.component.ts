import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent implements OnInit {
  private CurentUser = ''
  constructor(private _Auth:AuthService) { }

  ngOnInit() {
    this.CurentUser = this._Auth.CurentUsername
  }

}
