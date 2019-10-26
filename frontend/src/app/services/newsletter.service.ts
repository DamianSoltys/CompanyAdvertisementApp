import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class NewsletterService {

  constructor(private http:HttpClient) { }

  public saveToNewsletter(userEmail:string,companyId:number) {
    console.log(userEmail);
    return this.http.post(`http://localhost:8090/api/newsletter`,{email:userEmail,id:companyId});
  }
}
