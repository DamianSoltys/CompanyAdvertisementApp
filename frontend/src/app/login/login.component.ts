import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormControl,
  Validators
} from '@angular/forms';
import { UserLog, UserREST, UserReg } from '../classes/User';
import { LoginService } from '../services/login.service';
import { Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { storage_Avaliable } from '../classes/storage_checker';

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
  constructor(
    private fb: FormBuilder,
    private lgservice: LoginService,
    private router: Router
  ) {}

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
    const User_data = this.setUserData();
    this.checkIfLoginSuccess(User_data);
  }

  checkIfLoginSuccess(User_data: UserLog) {
    this.lgservice.Login(User_data).subscribe(
      (data: HttpResponse<any>) => {
        console.log(data.headers.get('Authorization'));
        if (data.status === 200) {
          this.login_error = false;
          this.showRequestMessage('error','Pomyślnie zalogowano!','');
          this.login_success = true;

          setTimeout(() => {
            this.loginStorageSet(data);
          }, 500);
        }
      },
      error => {
        console.log(error);
        this.showRequestMessage('error','','Coś poszło nie tak!');
        this.login_error = true;
      }
    );
  }

  private showRequestMessage(
    type: string,
    successMessage: string = this.success_message,
    errorMessage: string = this.error_message
  ) {
    if (type === 'success') {
      this.success_message = successMessage;
      this.error_message = '';
    } else {
      this.success_message = '';
      this.error_message = errorMessage;
    }
  }

  setUserData(): UserLog {
    const User_data = new UserLog();
    User_data.email = this.loginForm.get('email').value;
    User_data.password = this.loginForm.get('password').value;
    return User_data;
  }

  loginStorageSet(data: HttpResponse<any>) {
    if (storage_Avaliable('localStorage')) {
      let userObject: UserREST = data.body;

      localStorage.setItem('token', data.headers.get('Authorization'));
      localStorage.setItem('userREST', JSON.stringify(userObject));

      this.lgservice.ChangeLogged();
      this.router.navigate(['']);
      console.log('Użytkownik został zalogowany');
    } else {
      this.showRequestMessage('error','','Coś poszło nie tak!');
      this.login_error = true;
    }
  }
}
