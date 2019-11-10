import { Component, OnInit } from '@angular/core';
import { NewsletterService } from 'src/app/services/newsletter.service';
import { ActivatedRoute, Router } from '@angular/router';
import { PromotionItem } from 'src/app/classes/Newsletter';

@Component({
  selector: 'app-newsletter-list',
  templateUrl: './newsletter-list.component.html',
  styleUrls: ['./newsletter-list.component.scss']
})
export class NewsletterListComponent implements OnInit {
  public companyId:string;
  public newsletterList:PromotionItem[] = [];
  constructor(private nDataService:NewsletterService,private activatedRoute:ActivatedRoute,private router:Router) { }

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
        this.newsletterList = <PromotionItem[]>response.body;
      }
    });
  }

  public sendDelayedNewsletter() {

  }
  public goBack() {
    this.router.navigate(['/companyProfile',this.companyId]);
  }
}
