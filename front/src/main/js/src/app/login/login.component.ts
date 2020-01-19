import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';

import { AlertService, AuthService, UserService } from '@app/_services';

@Component({templateUrl: 'login.component.html',
            styleUrls: ['login.component.scss']})
export class LoginComponent implements OnInit {
    activeTab = 0;

    loginForm: FormGroup;
    loginLoading = false;
    loginSubmitted = false;
    returnUrl: string;

    registerForm: FormGroup;
    registerLoading = false;
    registerSubmitted = false;

    constructor(
        private formBuilder: FormBuilder,
        private route: ActivatedRoute,
        private router: Router,
        private authenticationService: AuthService,
        private alertService: AlertService,
        private userService: UserService,
    ) {
        // redirect to home if already logged in
        if (this.authenticationService.currentUserValue) { 
            this.router.navigate(['/']);
        }
    }

    ngOnInit() {
        this.loginForm = this.formBuilder.group({
            loginOrEmail: ['', Validators.required],
            password: ['', Validators.required],
            rememberMe: ['', '']
        });

        this.registerForm = this.formBuilder.group({
            email: ['', [Validators.required, Validators.email]],
            username: ['', Validators.required],
            password: ['', [Validators.required, Validators.minLength(6)]],
            confirmPassword: ['', [Validators.required]]
        },{
            validator: MustMatch('password', 'confirmPassword')
        });
        // get return url from route parameters or default to '/'
        this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
    }

    // convenience getter for easy access to form fields
    get f() { return this.loginForm.controls; }

    // convenience getter for easy access to form fields
    get fr() { return this.registerForm.controls; }

    onSubmit() {
        this.loginSubmitted = true;

        // stop here if form is invalid
        if (this.loginForm.invalid) {
            return;
        }

        this.loginLoading = true;
        this.authenticationService.login(this.f.loginOrEmail.value, this.f.password.value, this.f.rememberMe.value)
            // .pipe(first())
            .subscribe(
                data => {
                    this.router.navigate([this.returnUrl]);
                },
                error => {
                    this.alertService.error(error);
                    this.loginLoading = false;
                });
    }

    onSubmitRegister() {
        this.registerSubmitted = true;

        // stop here if form is invalid
        if (this.registerForm.invalid) {
            return;
        }

        this.registerLoading = true;
        this.authenticationService.register(this.fr.email.value, this.fr.username.value,
             this.fr.password.value, this.fr.confirmPassword.value)
            .subscribe(
                data => {
                    this.alertService.success('Регистрация завершена успешно!', true);
                    this.returnToLoginPage();
                },
                error => {
                    this.alertService.error(error);
                    this.registerLoading = false;
                });
    }

    returnToLoginPage() {
        this.activeTab = 0;
    }

    onTabChange(event) {
        this.alertService.clear();
    }
}

// custom validator to check that two fields match
export function MustMatch(controlName: string, matchingControlName: string) {
    return (formGroup: FormGroup) => {
        const control = formGroup.controls[controlName];
        const matchingControl = formGroup.controls[matchingControlName];

        if (matchingControl.errors && !matchingControl.errors.mustMatch) {
            // return if another validator has already found an error on the matchingControl
            return;
        }

        // set error on matchingControl if validation fails
        if (control.value !== matchingControl.value) {
            matchingControl.setErrors({ mustMatch: true });
        } else {
            matchingControl.setErrors(null);
        }
    }
}