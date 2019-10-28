import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NewsletterService {
  public destroyEditor = new Subject<any>();
  public getHtmlTemplate = new Subject<string>();
  public template = new Subject<any>();

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
