import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { BehaviorSubject, Subject } from 'rxjs';
import { PromotionItem } from '../classes/Newsletter';

@Injectable({
  providedIn: 'root'
})
export class NewsletterService {
  public destroyEditor = new Subject<any>();
  public getHtmlTemplate = new Subject<string>();
  public template = new Subject<any>();

  constructor(private http:HttpClient) { }

  public sendNewsletter(newsletterOptions:PromotionItem) {
    let subject = new Subject<boolean>();
    let httpParams = new HttpParams().set('companyId',newsletterOptions.companyId.toString());
    this.http.post(`http://localhost:8090/api/pi`,newsletterOptions,{params:httpParams}).subscribe(response=>{
      console.log(response);
      if(newsletterOptions.numberOfPhotos > 0) {
        let formData = new FormData();
        subject.next(true);
      } else {
        subject.next(true);
      }
    },error=>{
      console.log(error);
      subject.next(false);
    });
    return subject;
  }

  public sendDelayedNewsletter(promotionUUID:any) {
    let subject = new Subject<any>();
    this.http.get(`http://localhost:8090/api/pi/${promotionUUID}/confirmation`,{observe:'response'}).subscribe(response=>{
      console.log(response);
      subject.next(true);
    },error=>{
      console.log(error);
      subject.next(false);
    });
    return subject;
  }

  public getNewsletterData(companyId:string) {
    let subject = new Subject<any>();
    let httpParams = new HttpParams().set('companyId',companyId);
      this.http.get(`http://localhost:8090/api/pi`,{observe:'response',params:httpParams}).subscribe(response=>{
        subject.next(response);
      },error=>{
        subject.next(false);
      })
    return subject;
  }

  public saveToNewsletter(userEmail:string,companyId:number) {
    return this.http.post(`http://localhost:8090/api/newsletter`,{email:userEmail,id:companyId});
  }

  public sendSignUpData(token:string,type:string) {
    return this.http.get(`http://localhost:8090/api/newsletter/${type}?token=${token}`,{observe:'response'});
  }

  public sendSingOutData(token:string,type:string) {
    return this.http.get(`http://localhost:8090/api/newsletter/${type}?token=${token}`,{observe:'response'});
  }

  public getSubscriptionStatus(companyId:number,userId:number) {
    return this.http.get(`http://localhost:8090/api/newsletter/?userId=${userId}&companyId=${companyId}`,{observe:'response'});
  }
}
