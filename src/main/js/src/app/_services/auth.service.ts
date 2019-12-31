import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { map } from 'rxjs/operators';

import { environment } from '@environments/environment';
import { User } from '@app/_models';

import { AngularFireAuth } from 'angularfire2/auth';
import { from } from 'rxjs';
import { ILogin } from '@app/_models/ilogin';
import { IReg } from '@app/_models/ireg';

@Injectable({ providedIn: 'root' })
export class AuthService {
    private urlLogin = 'http://54.37.125.180:8080';

    private curLogin: ILogin = {
        usernameOrEmail: '*',
        password: '*'
    }
    private curRegistration: IReg = {
        username: '*',
        email: '*',
        password: '*',
        confirmPassword: '*'
    }

    private currentUserSubject: BehaviorSubject<User>;
    public currentUser: Observable<User>;

    constructor(private _http: HttpClient
                // , private firebaseAuth: AngularFireAuth ///TODO delete
        ) {
        this.currentUserSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('currentUser')));
        this.currentUser = this.currentUserSubject.asObservable();
    }

    public get currentUserValue(): User {
        return this.currentUserSubject.value;
    }

    login(username: string, password: string, rememberMe: boolean): Subject<User> {

        const subject = new Subject<User>();

        this.curLogin.usernameOrEmail = username;
        this.curLogin.password = password;
        this._http.post(this.urlLogin + '/auth/login', this.curLogin).toPromise()
        .then(data => {
            // const currentUser: User = this.getUserFromData(data);
            const currentUser: User = {///TODO check data from server
                id: 0,
                email: username,
                username: username,
                password: '',
                firstName: '',
                lastName: '',
                token: ''
            };
            if (rememberMe) {
                localStorage.setItem('currentUser', JSON.stringify(currentUser));
            }

            this.currentUserSubject.next(currentUser);

            subject.next(currentUser);
        })
        .catch(err => {
          subject.error(err);
        });

        //Firebase variant returns Promise instead of Observable
        // this.firebaseAuth
        // .auth
        // .signInWithEmailAndPassword(username, password)
        // .then(data => {
        //     const currentUser: User = this.getUserFromData(data);
        //     localStorage.setItem('currentUser', JSON.stringify(currentUser));
        //     this.currentUserSubject.next(currentUser);

        //     subject.next(currentUser);
        // })
        // .catch(err => {
        //   subject.error(err);
        // });
        return subject;
    }

    register(email: string, username: string, password: string, confirmPassword: string): Observable<any> {
        this.curRegistration.username = username;
        this.curRegistration.password = password;
        this.curRegistration.email = email;
        this.curRegistration.confirmPassword = confirmPassword;
        return this._http.post(this.urlLogin + '/auth/register', this.curRegistration);
        //Firebase variant
        // return from(this.firebaseAuth
        //     .auth
        //     .createUserWithEmailAndPassword(user.email, user.password));
    }

    logout(): Subject<boolean> {
        const subject = new Subject<boolean>();

        //TODO logout on server
        localStorage.removeItem('currentUser');
        this.currentUserSubject.next(null);
        subject.next(true);

        //Firebase variant
        // this.firebaseAuth
        // .auth
        // .signOut().then(res => {
        //     // remove user from local storage to log user out
        //     localStorage.removeItem('currentUser');
        //     this.currentUserSubject.next(null);
        //     subject.next(true);
        // }).catch(err => {console.log(err); subject.error(err); });

        return subject;
    }

    getUserFromData(data): User {
        return {id: data.user.uid,
        username: data.user.email,
        password: '',
        firstName: data.user.email,
        lastName: data.user.displayName,
        token: data.user.l} as User;
    }
}