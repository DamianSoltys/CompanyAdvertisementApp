import { Component, OnInit } from '@angular/core';
import { NewsletterService } from 'src/app/services/newsletter.service';
import { ActivatedRoute, Router } from '@angular/router';
import { PromotionItem, PromotionItemResponse, SendingStatus } from 'src/app/classes/Newsletter';
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
        console.log(response)
        this.newsletterList = <PromotionItemResponse[]>response.body;
        console.log(this.newsletterList)
        this.convertUnixToDate();
      }
    });
  }

  private convertUnixToDate() {
    moment.locale('pl');
    this.newsletterList.map(newsletter=>{
      newsletter.addedTime = moment.unix(newsletter.addedTime).format('LLL');
      if(newsletter.sendingStatus[0].plannedSendingAt) {
        newsletter.sendingStatus[0].plannedSendingAt = moment.unix(newsletter.sendingStatus[0].plannedSendingAt).format('LLL');
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
  public checkStatus(status:SendingStatus[]) {
    if(status[0].sendingStatus == "waiting_for_action") { //only_at_will
      return true;
    } else {
      return false;
    }
  }

  public showSendTime(status:string) {
    if(status == "delayed") {
      return true;
    } else {
      return false;
    }
  }
  
  public goBack() {
    this.router.navigate(['/companyProfile',this.companyId]);
  }
}
