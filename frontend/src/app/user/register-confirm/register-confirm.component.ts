import { Component, OnInit, AfterViewInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { RegisterAuthService } from 'src/app/services/register-auth.service';

@Component({
  selector: 'app-register-confirm',
  templateUrl: './register-confirm.component.html',
  styleUrls: ['./register-confirm.component.scss']
})
export class RegisterConfirmComponent implements OnInit,AfterViewInit {
  param:string;
  constructor(private activatedRoute: ActivatedRoute, private router: Router, private registerAuth: RegisterAuthService) { }

  ngOnInit() {

  this.activatedRoute.params.subscribe((params) => {
    this.param = params['auth'];
  });
  }
  ngAfterViewInit(){
    this.registerAuth.registerAuth(this.param).subscribe((response) => {
      console.log(response);
    }, (error) => {
      console.log(error);
    });
  }

}
