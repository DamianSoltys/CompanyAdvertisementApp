import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormControl,
  Validators
} from '@angular/forms';
import { UserLog, UserREST, UserReg, PersonalData } from '../classes/User';
import { LoginService } from '../services/login.service';
import { Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { storage_Avaliable } from '../classes/storage_checker';
import { UserService } from '../services/user.service';
import { SnackbarService, SnackbarType } from '../services/snackbar.service';
import { PersonalDataService } from '../services/personal-data.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  constructor(
    private fb: FormBuilder,
    private lgservice: LoginService,
    private router: Router,
    private pDataService:PersonalDataService,
    private snackbarService:SnackbarService,
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
          this.snackbarService.open({
            message:'Pomyślnie zalogowano',
            snackbarType:SnackbarType.success,
          });          
          setTimeout(() => {
            this.loginStorageSet(data);
            this.pDataService.getPersonalDataObject();
          }, 500);
        }
      },
      error => {
        console.log(error);
        this.snackbarService.open({
          message:'Coś poszło nie tak!',
          snackbarType:SnackbarType.error,
        });
      }
    );
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
      this.snackbarService.open({
        message:'Coś poszło nie tak!',
        snackbarType:SnackbarType.error,
      });
    }
  }
}
