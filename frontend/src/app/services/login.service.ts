import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserLog } from '../classes/User';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private http: HttpClient) {

   }

   Login(User_Data: UserLog):Observable<any> {
    return this.http.post('http://localhost:8090/auth/login',
    {'email': User_Data.email, 'password': User_Data.password}, {observe: 'response'});
  }

}
