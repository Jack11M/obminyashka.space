import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { environment } from '@environments/environment';
import { User } from '@app/_models';

import { AngularFireAuth } from '@angular/fire/auth';
import { from } from 'rxjs';
@Injectable({ providedIn: 'root' })
export class UserService {
    constructor(private firebaseAuth: AngularFireAuth, private http: HttpClient) { }

    getAll() {
        return this.http.get<User[]>(`${environment.apiUrl}/users`);
    }

    getById(id: number) {
        return this.http.get(`${environment.apiUrl}/users/${id}`);
    }

    register(user: User) {
        return from(this.firebaseAuth
            .auth
            .createUserWithEmailAndPassword(user.email, user.password));
    }

    update(user: User) {
        return this.http.put(`${environment.apiUrl}/users/${user.id}`, user);
    }

    delete(id: number) {
        return this.http.delete(`${environment.apiUrl}/users/${id}`);
    }
}
