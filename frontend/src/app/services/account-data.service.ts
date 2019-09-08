import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { PersonalData } from '../classes/User';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AccountDataService {

  constructor(private http: HttpClient) { }

  public deleteAccount(userId:number) {
    return this.http.delete(`http://localhost:8090/api/user/${userId}`);
  }

  public changePassword(currentPassword:string,newPassword:string,userId:number) {
    return this.http.patch(`http://localhost:8090/api/user/${userId}`,{
      'currentPassword': currentPassword,
      'newPassword': newPassword
    });
  }
}
