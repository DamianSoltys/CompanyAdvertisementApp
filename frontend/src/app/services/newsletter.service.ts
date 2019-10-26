import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class NewsletterService {

  constructor(private http:HttpClient) { }

  public saveToNewsletter(userEmail:string,companyId:number) {
    return this.http.post(`http://localhost:8090/api/newsletter`,{email:userEmail,id:companyId});
  }

  public sendSignUpData(token:string,type:string) {
    return this.http.get(`http://localhost:8090/api/newsletter/${type}?token=${token}`,{observe:'response'});
  }

  public sendSingOutData(token:string,type:string) {
    return this.http.get(`http://localhost:8090/api/newsletter/${type}?token=${token}`,{observe:'response'});
  }
}
