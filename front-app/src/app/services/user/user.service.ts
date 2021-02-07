import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { observable, Observable } from 'rxjs';
import { User } from 'src/app/models/User';

const BASE_PATH = 'http://localhost:8765/api/user-service/users';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  getUser(userId: number): any {
    return this.http.get(BASE_PATH + '/' + userId);
  }

  getCustomers(): Observable<User[]> {
    return this.http.get<User[]>(BASE_PATH + '/customers');
  }

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(BASE_PATH);
  }

  deleteUser(userId: number): any {
    return this.http.delete(BASE_PATH + '/' + userId);
  }

  addUser(user: User): any {
    return this.http.post(BASE_PATH + '/sign-up',
      {
        email: user.email,
        name: user.name,
        surname: user.surname,
        ssn: user.ssn,
        password: user.password,
        confirmPassword: user.confirmPassword,
        userRole: user.role
      }
    );
  }

  addCustomer(user: User): any {
    return this.http.post(BASE_PATH,
      {
        name: user.name,
        surname: user.surname,
        ssn: user.ssn
      });
  }
}
