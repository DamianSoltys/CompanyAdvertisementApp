import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { BehaviorSubject, Subject } from 'rxjs';
import { PromotionItem, SendingStrategy } from '../interfaces/Newsletter';
export interface newsletterResponse {
  promotionItemPhotosUUIDsDto: string[];
  promotionItemUUID: string;
  sendingFinished: boolean;
}
@Injectable({
  providedIn: 'root'
})
export class NewsletterService {
  public destroyEditor = new Subject<any>();
  public getHtmlTemplate = new Subject<string>();
  public template = new Subject<any>();

  constructor(private http: HttpClient) { }

  public sendNewsletter(newsletterOptions: PromotionItem, fileList: File[]) {
    let subject = new Subject<boolean>();
    let httpParams = new HttpParams().set('companyId', newsletterOptions.companyId.toString());

    this.http.post(`http://localhost:8090/api/pi`, newsletterOptions, { params: httpParams }).subscribe(response => {
      let responseBody = <newsletterResponse>response;

      if (fileList) {
        let formData = new FormData();
        for (let i = 0; i < fileList.length; i++) {
          formData.append(responseBody.promotionItemPhotosUUIDsDto[i], fileList[i]);
          formData.append(responseBody.promotionItemPhotosUUIDsDto[i], 'Value');
        }
        this.http.put(`http://localhost:8090/static/pi/${responseBody.promotionItemUUID}`, formData).subscribe(response => {
          setTimeout(() => {
            this.http.put(`http://localhost:8090/api/pi/${responseBody.promotionItemUUID}/adding`, { observe: 'response' }).subscribe(response => {

              if (response) {
                subject.next(true);
              } else {
                setTimeout(() => {
                  this.http.put(`http://localhost:8090/api/pi/${responseBody.promotionItemUUID}/adding`, { observe: 'response' }).subscribe(response => {

                    if (response) {
                      subject.next(true);
                    } else {
                      subject.next(false);
                    }
                  }, error => {
                    console.log(error);
                    subject.next(false);
                  });
                }, 3000);
              }
            }, error => {
              console.log(error);
              subject.next(false);
            });
          }, 3000)

        }, error => {
          console.log(error);
          subject.next(false);
        });

      } else {
        subject.next(true);
      }
    }, error => {
      console.log(error);
      subject.next(false);
    });

    return subject;
  }

  public sendDelayedNewsletter(promotionUUID: any) {
    let subject = new Subject<any>();

    this.http.put(`http://localhost:8090/api/pi/${promotionUUID}/sending`, { observe: 'response' }).subscribe(response => {
      subject.next(true);
    }, error => {
      console.log(error);
      subject.next(false);
    });

    return subject;
  }

  public getNewsletterData(companyId: string) {
    let subject = new Subject<any>();
    let httpParams = new HttpParams().set('companyId', companyId);

    this.http.get(`http://localhost:8090/api/pi`, { observe: 'response', params: httpParams }).subscribe(response => {
      subject.next(response);
    }, error => {
      subject.next(false);
    });

    return subject;
  }

  public saveToNewsletter(userEmail: string, companyId: number) {
    return this.http.post(`http://localhost:8090/api/newsletter`, { email: userEmail, id: companyId });
  }

  public sendSignUpData(token: string, type: string) {
    return this.http.get(`http://localhost:8090/api/newsletter/${type}?token=${token}`, { observe: 'response' });
  }

  public sendSingOutData(token: string, type: string) {
    return this.http.get(`http://localhost:8090/api/newsletter/${type}?token=${token}`, { observe: 'response' });
  }

  public getSubscriptionStatus(companyId: number, userId: number) {
    return this.http.get(`http://localhost:8090/api/newsletter/?userId=${userId}&companyId=${companyId}`, { observe: 'response' });
  }
}
