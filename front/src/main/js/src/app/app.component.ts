import { Component } from '@angular/core';
import { Router } from '@angular/router';

import { AuthService } from './_services/auth.service';
import { User } from './_models';

@Component({
    selector: 'app',
    templateUrl: 'app.component.html'
})
export class AppComponent {
    currentUser: User;

    constructor(
        private router: Router,
        private authService: AuthService
    ) {
        this.authService.currentUser.subscribe(x => this.currentUser = x);
    }

    logout() {
        // this.AuthService.logout();
        // this.router.navigate(['/login']);
    }
}