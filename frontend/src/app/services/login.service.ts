import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { UserLog } from '../classes/User';
import { Observable, BehaviorSubject, observable } from 'rxjs';
import { storage_Avaliable } from '../classes/storage_checker';
import { Route, Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  Logged = new BehaviorSubject(this.CheckLogged());
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
    observe: 'response' as 'response'
  };
  constructor(private http: HttpClient, private router: Router) {}

  public Login(User_Data: UserLog): Observable<any> {
    return this.http.post(
      'http://localhost:8090/auth/login',
      { email: User_Data.email, password: User_Data.password },
      this.httpOptions
    );
  }
  public ChangeLogged() {
    this.Logged.next(!this.Logged.value);
  }
  public CheckLogged() {
    if (localStorage.getItem('token')) {
      return true;
    } else {
      return false;
    }
  }
  public logoutStorageClean() {
    if (storage_Avaliable('localStorage')) {
      localStorage.removeItem('token');
      localStorage.removeItem('userREST');
      localStorage.removeItem('naturalUserData');
      localStorage.removeItem('actualPosition');
      this.ChangeLogged();
      this.router.navigate(['']);
    } else {
      console.log('Storage nie jest dostępny');
    }
  }
}
