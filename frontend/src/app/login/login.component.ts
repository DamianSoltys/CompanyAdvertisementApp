import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { UserLog } from '../classes/User';
import { LoginService } from '../services/login.service';
import { Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  login_error = false;
  login_success = false;
  success_message: string;
  error_message: string;
  constructor(private fb: FormBuilder, private lgservice: LoginService,
     private router: Router) { }

  ngOnInit() {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]]
    });
  }


  get logForm() {
    return this.loginForm.controls;

  }
  onSubmit() {
    const User_data = new UserLog();
    User_data.email = this.loginForm.get('email').value;
    User_data.password = this.loginForm.get('password').value;
    this.lgservice.Login(User_data).subscribe((data: HttpResponse<any>) => {
      console.log(data.headers.get('Authorization'));
      if (data.body.data === 'OK') {
        this.login_error = false;
        this.success_message = 'Pomyślnie zalogowano';
        this.login_success = true;
        setTimeout(() => {
          localStorage.setItem('token', data.headers.get('Authorization'));
          this.lgservice.ChangeLogged();
          this.router.navigate(['']);
          console.log('Użytkownik został zalogowany');
        }, 500);

      }
    }, (error) => {
      console.log(error);
      this.error_message = error.error;
      this.login_error = true;

    });
  }

}
