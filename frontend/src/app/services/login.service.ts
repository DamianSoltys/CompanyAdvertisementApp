import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { UserLog } from '../classes/User';
import { Observable, BehaviorSubject, observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  Logged = new BehaviorSubject(false);
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
    observe: 'response' as 'response'
  };
  constructor(private http: HttpClient) {

   }

   Login(User_Data: UserLog): Observable<any> {
    return this.http.post('http://localhost:8090/auth/login',
    {'email': User_Data.email, 'password': User_Data.password}, this.httpOptions);
  }
  ChangeLogged() {
    this.Logged.next(!this.Logged.value);
  }
  CheckLogged() {
    if (localStorage.getItem('token')) {
      return true;
    } else {
      return false;
    }
  }

}
