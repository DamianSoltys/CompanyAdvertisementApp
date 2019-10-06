import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { PersonalData, UserREST } from '../classes/User';
import { Observable } from 'rxjs';
import { storage_Avaliable } from '../classes/storage_checker';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http:HttpClient) { }

  public getActualUser(userId): Observable<any> {
    return this.http.get(`http://localhost:8090/api/user/${userId}`,{observe: 'response'});
  }

  public updateUser() {
    let userObject:UserREST = JSON.parse(localStorage.getItem('userREST'));
      this.getActualUser(userObject.userID).subscribe(
        response => {
          if (storage_Avaliable('localStorage')) {
            const userNewObject: UserREST = response.body;
            console.log(userNewObject);
            localStorage.setItem('userREST', JSON.stringify(userNewObject));
          } 
        },
        error => {
          console.log(error);
        }
      );
    
  }
}
