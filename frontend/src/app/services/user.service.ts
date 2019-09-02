import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { PersonalData } from '../classes/User';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http:HttpClient) { }

  getActualUser(userId): Observable<any> {
    return this.http.get(`http://localhost:8090/api/user/${userId}`,{observe: 'response'});
  }
}
