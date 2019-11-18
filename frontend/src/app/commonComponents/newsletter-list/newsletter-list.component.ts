import { Component, OnInit } from '@angular/core';
import { NewsletterService } from 'src/app/services/newsletter.service';
import { ActivatedRoute, Router } from '@angular/router';
import { PromotionItem, PromotionItemResponse } from 'src/app/classes/Newsletter';
import * as moment from 'moment';
import 'moment/locale/pl';
import { SnackbarService, SnackbarType } from 'src/app/services/snackbar.service';

@Component({
  selector: 'app-newsletter-list',
  templateUrl: './newsletter-list.component.html',
  styleUrls: ['./newsletter-list.component.scss']
})
export class NewsletterListComponent implements OnInit {
  public companyId:string;
  public newsletterList:PromotionItemResponse[] = [];
  constructor(private nDataService:NewsletterService,private activatedRoute:ActivatedRoute,private router:Router,private snackbar:SnackbarService) { }

  ngOnInit() {
    this.activatedRoute.parent.params.subscribe(params=>{
      this.companyId = params['id'];
      this.getNewsletters();
    });
  }

  private getNewsletters() {
    this.nDataService.getNewsletterData(this.companyId).subscribe(response=>{
      if(response) {
        console.log(response);
        this.newsletterList = <PromotionItemResponse[]>response.body;
        this.convertUnixToDate();
      }
    });
  }

  private convertUnixToDate() {
    moment.locale('pl');
    this.newsletterList.map(newsletter=>{
      newsletter.addedTime = moment.unix(newsletter.addedTime).format('LLL');
      if(newsletter.plannedSendingTime) {
        newsletter.plannedSendingTime = moment.unix(newsletter.plannedSendingTime).format('LLL');
      }
    });
  }

  public sendDelayedNewsletter(promotionUUID:any) {
    this.nDataService.sendDelayedNewsletter(promotionUUID).subscribe(response=>{
      if(response) {
        this.getNewsletters();
        this.snackbar.open({
          message:'Newsletter zostal pomyślnie wysłany',
          snackbarType:SnackbarType.success,
        });
      } else {
        this.snackbar.open({
          message:'Nie udało się wysłać newslettera',
          snackbarType:SnackbarType.error,
        });
      }
    });
  }
  public checkStatus(status:string) {
    if(status == "SENT") {
      return false;
    } else {
      return true;
    }
  }
  public goBack() {
    this.router.navigate(['/companyProfile',this.companyId]);
  }
}
