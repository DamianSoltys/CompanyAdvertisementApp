import { Component, OnInit } from '@angular/core';
import { NewsletterService } from 'src/app/services/newsletter.service';
import { ActivatedRoute, Router } from '@angular/router';
import { PromotionItem, PromotionItemResponse, SendingStatus, SendStatus, SendingStrategy, Destination, SendStatusPL } from 'src/app/interfaces/Newsletter';
import * as moment from 'moment';
import 'moment/locale/pl';
import { SnackbarService, SnackbarType } from 'src/app/services/snackbar.service';

@Component({
  selector: 'app-newsletter-list',
  templateUrl: './newsletter-list.component.html',
  styleUrls: ['./newsletter-list.component.scss']
})
export class NewsletterListComponent implements OnInit {
  public companyId: string;
  public newsletterList: PromotionItemResponse[] = [];

  constructor(
    private nDataService: NewsletterService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private snackbar: SnackbarService) { }

  ngOnInit() {
    this.activatedRoute.parent.params.subscribe(params => {
      this.companyId = params['id'];
      this.getNewsletters();
    });
  }

  public sendDelayedNewsletter(promotionUUID: any) {
    this.nDataService.sendDelayedNewsletter(promotionUUID).subscribe(response => {
      if (response) {
        this.getNewsletters();
        this.snackbar.open({
          message: 'Newsletter zostal pomyślnie wysłany',
          snackbarType: SnackbarType.success,
        });
      } else {
        this.snackbar.open({
          message: 'Nie udało się wysłać newslettera',
          snackbarType: SnackbarType.error,
        });
      }
    });
  }

  public checkStatus(status: string) {
    if (status === SendStatusPL.WAITING) { //only_at_will
      return true;
    } else {
      return false;
    }
  }

  public showSendTime(status: string) {
    if (status == SendStatusPL.DELAYED) {
      return true;
    } else {
      return false;
    }
  }

  public goBack() {
    this.router.navigate(['/companyProfile', this.companyId]);
  }

  private getNewsletters() {
    this.nDataService.getNewsletterData(this.companyId).subscribe(response => {
      if (response) {
        this.newsletterList = <PromotionItemResponse[]>response.body;
        this.translateText();
        this.convertUnixToDate();
        this.removeUnwantedString();
      }
    });
  }

  private removeUnwantedString() {
    this.newsletterList.map(newsletter => {
      if (newsletter.sendingStatus) {
        if (newsletter.sendingStatus[0].detail) {
          newsletter.sendingStatus[0].detail = newsletter.sendingStatus[0].detail.replace('Post resource: ', '');
        }
      }
    });
  }

  private translateText() {
    this.newsletterList.map(newsletter => {
      if (newsletter.sendingStatus) {
        switch (newsletter.sendingStatus[0].sendingStatus) {
          case SendStatus.DELAYED: {
            newsletter.sendingStatus[0].sendingStatus = 'Wysyłka opóźniona';
            break;
          }
          case SendStatus.SENT: {
            newsletter.sendingStatus[0].sendingStatus = 'Wysłany';
            break;
          }
          case SendStatus.WAITING: {
            newsletter.sendingStatus[0].sendingStatus = 'Oczekiwanie na wysłanie';
            break;
          }
        }

        switch (newsletter.sendingStatus[0].destination.toUpperCase()) {
          case Destination.FB: {
            newsletter.sendingStatus[0].destination = "Facebook"
            break;
          }
          case Destination.TWITTER: {
            newsletter.sendingStatus[0].destination = 'Twitter';
            break;
          }
          case Destination.NEWSLETTER: {
            newsletter.sendingStatus[0].destination = 'Newsletter';
            break;
          }
        }
      }
    });
  }

  private convertUnixToDate() {
    moment.locale('pl');
    this.newsletterList.map(newsletter => {
      newsletter.addedTime = moment.unix(newsletter.addedTime).format('LLL');

      if (newsletter.sendingStatus) {
        if (newsletter.sendingStatus[0].plannedSendingAt) {
          newsletter.sendingStatus[0].plannedSendingAt = moment.unix(newsletter.sendingStatus[0].plannedSendingAt).format('LLL');
        }

        if (newsletter.sendingStatus[0].sendAt) {
          newsletter.sendingStatus[0].sendAt = moment.unix(newsletter.sendingStatus[0].sendAt).format('LLL');
        }
      }
    });
  }
}
