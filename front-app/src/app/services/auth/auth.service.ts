import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

const USER_API = 'http://localhost:8765/api/user-service/users/';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) { }

  login(credentials): Observable<any> {
    return this.http.post(USER_API + 'sign-in', {
      email: credentials.email,
      password: credentials.password
    }, httpOptions);
  }

  register(user): Observable<any> {
    return this.http.post(USER_API + 'sign-up', {
      email: user.email,
      name: user.name,
      surname: user.surname,
      password: user.password,
      confirmPassword: user.confirmPassword,
      ssn: user.ssn
    }, httpOptions);
  }
}
