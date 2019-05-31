import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserReg } from '../classes/User';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  constructor(private http: HttpClient) {

  }

  Register(User_Data: UserReg): Observable<any> {
    return this.http.post('http://localhost:8090/auth/registration',
    {'name': User_Data.name, 'email': User_Data.email, 'password': User_Data.password}, {observe: 'response'});
  }
}
