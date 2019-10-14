import {
  Component,
  OnInit,
  Renderer2,
  ViewChild,
  ElementRef
} from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormControl,
  Validators,
  FormArray
} from '@angular/forms';
import { BehaviorSubject } from 'rxjs';
import { HttpResponse } from '@angular/common/http';

import { UserREST } from 'src/app/classes/User';
import { storage_Avaliable } from 'src/app/classes/storage_checker';
import { UserService } from 'src/app/services/user.service';
import { AccountDataService } from 'src/app/services/account-data.service';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/services/login.service';
import { SnackbarService, SnackbarType } from 'src/app/services/snackbar.service';

@Component({
  selector: 'app-account-data',
  templateUrl: './account-data.component.html',
  styleUrls: ['./account-data.component.scss']
})
export class AccountDataComponent implements OnInit {
  public showEditingForm = new BehaviorSubject<boolean>(true);
  public showData = new BehaviorSubject<boolean>(false);
  public accountDataForm: FormGroup;
  public userObject: UserREST;

  constructor(
    private fb: FormBuilder,
    private renderer: Renderer2,
    private userService: UserService,
    private accountService: AccountDataService,
    private router: Router,
    private lgService: LoginService,
    private snackbarService:SnackbarService
  ) {}

  ngOnInit() {
    this.accountDataForm = this.fb.group({
      oldPassword: ['', [Validators.required]],
      newPassword: [
        '',
        [
          Validators.required,
          Validators.pattern(
            new RegExp(/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{5,}$/)
          )
        ]
      ],
      checkPassword: ['', [Validators.required]]
    });
    this.checkForUserData();
  }

  private checkForUserData() {
    this.userObject = JSON.parse(localStorage.getItem('userREST'));
    if (storage_Avaliable('localStorage') && this.userObject) {
      this.showAccountData();
    } else {
      this.snackbarService.open({
        message:'Coś poszło nie tak!',
        snackbarType:SnackbarType.error,
      });
    }
  }

  private updateUserObject() {
    this.userService.getActualUser(this.userObject.userID).subscribe(
      response => {
        console.log(response);
        if (storage_Avaliable('localStorage')) {
          const userNewObject: UserREST = response.body;
          localStorage.setItem('userREST', JSON.stringify(userNewObject));
        } else {
          console.log('Nie udało się zapisać nowych danych usera');
        }
      },
      error => {
        console.log(error);
      }
    );
  }

  public showAccountData(e?: Event) {
    if (e) {
      e.preventDefault();
    }
    this.showEditingForm.next(false);
    this.showData.next(true);
  }

  public showEditForm() {
    this.showData.next(false);
    this.showEditingForm.next(true);
  }

  get form() {
    return this.accountDataForm.controls;
  }

  public onSubmit() {
    if (this.form.newPassword.value === this.form.checkPassword.value) {
      this.accountService
        .changePassword(
          this.form.oldPassword.value,
          this.form.newPassword.value,
          this.userObject.userID
        )
        .subscribe(
          response => {
            this.snackbarService.open({
              message:'Hasło zostało zmienione',
              snackbarType:SnackbarType.success,
            });
            let newToken = window.btoa(
              `${this.userObject.emailAddress}:${this.form.newPassword.value}`
            );
            localStorage.setItem('token', newToken);
            this.updateUserObject();
            this.accountDataForm.reset();
            setTimeout(() => {
              this.showAccountData();
            }, 1000);
          },
          error => {
            this.snackbarService.open({
              message:'Coś poszło nie tak!',
              snackbarType:SnackbarType.error,
            });
          }
        );
    } else {
      this.snackbarService.open({
        message:'Hasła się różnią!',
        snackbarType:SnackbarType.error,
      });
    }
  }

  public deleteAccount() {
    this.accountService.deleteAccount(this.userObject.userID).subscribe(
      response => {
        console.log(response);
        this.snackbarService.open({
          message:'Konto zostało usunięte!',
          snackbarType:SnackbarType.success,
        });
        this.lgService.logoutStorageClean();
      },
      error => {
        this.snackbarService.open({
          message:'Coś poszło nie tak!',
          snackbarType:SnackbarType.error,
        });
      }
    );
  }
}
