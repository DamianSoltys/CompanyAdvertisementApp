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

import { UserREST } from '@interfaces/User';
import { storage_Avaliable } from '@interfaces/storage_checker';
import { UserService } from '@services/user.service';
import { AccountDataService } from '@services/account-data.service';
import { Router } from '@angular/router';
import { LoginService } from '@services/login.service';
import { SnackbarService, SnackbarType } from '@services/snackbar.service';
import { FormErrorService } from '@services/form-error.service';

@Component({
  selector: 'app-account-data',
  templateUrl: './account-data.component.html',
  styleUrls: ['./account-data.component.scss']
})
export class AccountDataComponent implements OnInit {
  public showEditingForm = new BehaviorSubject<boolean>(true);
  public showData = new BehaviorSubject<boolean>(false);
  public userObject: UserREST;
  public accountDataForm = this.fb.group({
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

  constructor(
    private fb: FormBuilder,
    private renderer: Renderer2,
    private userService: UserService,
    private accountService: AccountDataService,
    private router: Router,
    private lgService: LoginService,
    private snackbarService: SnackbarService,
    private formErrorService: FormErrorService,
    private uDataService: UserService,
  ) { }

  ngOnInit() {
    this.checkForUserData();
  }

  private checkForUserData() {
    this.userObject = JSON.parse(localStorage.getItem('userREST'));

    if (storage_Avaliable('localStorage') && this.userObject) {
      this.showAccountData();
    } else {
      this.formErrorService.open({
        message: 'Nie udało się pobrać danych!'
      });
    }
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
      this.accountService.changePassword(
        this.form.oldPassword.value,
        this.form.newPassword.value,
        this.userObject.userID
      ).subscribe(
        response => {
          this.snackbarService.open({
            message: 'Hasło zostało zmienione',
            snackbarType: SnackbarType.success,
          });
          let newToken = window.btoa(
            `${this.userObject.emailAddress}:${this.form.newPassword.value}`
          );
          localStorage.setItem('token', newToken);
          this.uDataService.updateUser().subscribe(() => {
            this.accountDataForm.reset();
            this.showAccountData();
          });
        },
        error => {
          this.formErrorService.open({
            message: 'Nie udało się zmienić danych!'
          });
        }
      );
    } else {
      this.formErrorService.open({
        message: 'Hasła się od siebie różnią!'
      });
    }
  }

  public deleteAccount() {
    this.accountService.deleteAccount(this.userObject.userID).subscribe(
      response => {
        this.snackbarService.open({
          message: 'Konto zostało usunięte!',
          snackbarType: SnackbarType.success,
        });
        this.lgService.logoutStorageClean();
      },
      error => {
        this.formErrorService.open({
          message: 'Nie udało się usunąć danych!'
        });
      }
    );
  }
}
