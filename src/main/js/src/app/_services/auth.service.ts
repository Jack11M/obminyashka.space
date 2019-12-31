import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { map } from 'rxjs/operators';

import { environment } from '@environments/environment';
import { User } from '@app/_models';

import { AngularFireAuth } from 'angularfire2/auth';
import { from } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
    private currentUserSubject: BehaviorSubject<User>;
    public currentUser: Observable<User>;

    constructor(private firebaseAuth: AngularFireAuth, private http: HttpClient) {
        this.currentUserSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('currentUser')));
        this.currentUser = this.currentUserSubject.asObservable();
    }

    public get currentUserValue(): User {
        return this.currentUserSubject.value;
    }

    login(username: string, password: string): Subject<User> {

        const subject = new Subject<User>();

        this.firebaseAuth
        .auth
        .signInWithEmailAndPassword(username, password)
        .then(data => {
            const currentUser: User = this.getUserFromData(data);
            localStorage.setItem('currentUser', JSON.stringify(currentUser));
            this.currentUserSubject.next(currentUser);

            subject.next(currentUser);
        })
        .catch(err => {
          console.log('Something went wrong:', err.message);
          subject.error(err);
        });
        return subject;
    }

    logout(): Subject<boolean> {
        const subject = new Subject<boolean>();

        this.firebaseAuth
        .auth
        .signOut().then(res => {
            // remove user from local storage to log user out
            localStorage.removeItem('currentUser');
            this.currentUserSubject.next(null);
            subject.next(true);
        }).catch(err => {console.log(err); subject.error(err); });

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