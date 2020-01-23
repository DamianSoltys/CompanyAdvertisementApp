import { Component, OnInit, AfterViewInit, OnDestroy } from '@angular/core';
import { NewsletterService } from '@services/newsletter.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-newsletter-auth',
  templateUrl: './newsletter-auth.component.html',
  styleUrls: ['./newsletter-auth.component.scss']
})
export class NewsletterAuthComponent implements OnInit, AfterViewInit, OnDestroy {
  public time: number;
  private type: string;
  private token: string;
  private interval;

  constructor(private nDataService: NewsletterService, private activatedRoute: ActivatedRoute, private router: Router, ) { }

  ngOnInit() {
    this.activatedRoute.params.subscribe(params => {
      this.type = params['type'];
      this.token = params['token'];
    });
    this.time = 10;
  }

  ngAfterViewInit() {
    if (this.type === 'signup') {
      this.nDataService.sendSignUpData(this.token, this.type).subscribe(
        response => {
          this.interval = setInterval(() => {
            this.time--;
          }, 1000);
          setTimeout(() => {
            this.router.navigate(['/home']);
          }, 9000);
        },
        error => {
          console.log(error);
        }
      );
    } else {
      this.nDataService.sendSingOutData(this.token, this.type).subscribe(
        response => {
          this.interval = setInterval(() => {
            this.time--;
          }, 1000);
          setTimeout(() => {
            this.router.navigate(['/home']);
          }, 9000);
        },
        error => {
          console.log(error);
        }
      );
    }
  }

  ngOnDestroy() {
    clearInterval(this.interval);
  }

}
