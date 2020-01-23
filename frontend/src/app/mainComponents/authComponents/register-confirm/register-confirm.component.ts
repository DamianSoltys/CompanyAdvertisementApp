import { Component, OnInit, AfterViewInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { RegisterAuthService } from '@services/register-auth.service';

@Component({
  selector: 'app-register-confirm',
  templateUrl: './register-confirm.component.html',
  styleUrls: ['./register-confirm.component.scss']
})
export class RegisterConfirmComponent implements OnInit, AfterViewInit, OnDestroy {
  public time: number;
  private param: string;
  private interval;

  constructor(private activatedRoute: ActivatedRoute, private router: Router, private registerAuth: RegisterAuthService) { }

  ngOnInit() {
    this.activatedRoute.params.subscribe(params => {
      this.param = params['auth'];
    });
    this.time = 10;
  }

  ngAfterViewInit() {
    this.registerAuth.registerAuth(this.param).subscribe(
      response => {
        this.interval = setInterval(() => {
          this.time--;
        }, 1000);
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 9000);
      },
      error => {
        console.log(error);
      }
    );
  }

  ngOnDestroy() {
    clearInterval(this.interval);
  }
}
