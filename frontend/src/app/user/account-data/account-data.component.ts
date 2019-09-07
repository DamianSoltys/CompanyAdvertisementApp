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

  public successMessage: string = '';
  public errorMessage: string = '';
  private successMessageText = 'Akcja została zakończona pomyślnie';
  private errorMessageText = 'Akcja niepowiodła się';

  constructor(
    private fb: FormBuilder,
    private renderer: Renderer2,
    private userService: UserService
  ) {}

  ngOnInit() {
    this.accountDataForm = this.fb.group({
      password: [
        '',
        [
          Validators.required,
          Validators.pattern(
            new RegExp(/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{5,}$/)
          )
        ]
      ],
      checkpassword: ['', [Validators.required]]
    });  
    this.checkForUserData();
  }

  private checkForUserData() {
    this.userObject = JSON.parse(localStorage.getItem('userREST'));
    if(storage_Avaliable('localStorage') && this.userObject) {
      this.showAccountData();
    } else {
      this.showRequestMessage('error','','Coś poszło nie tak');
    }
  }

  private showRequestMessage (
    type: string,
    successMessage: string = this.successMessageText,
    errorMessage: string = this.errorMessageText
  ) {
    if (type === 'success') {
      this.successMessage = successMessage;
      this.errorMessage = '';
    } else {
      this.successMessage = '';
      this.errorMessage = errorMessage;
    }
  }

  private clearRequestMessage() {
    this.successMessage = '';
    this.errorMessage = '';
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
    this.clearRequestMessage();
    this.showEditingForm.next(false);
    this.showData.next(true);
  }

  public showEditForm() {
    this.clearRequestMessage();
    this.showData.next(false);
    this.showEditingForm.next(true);
  }

  get form() {
    return this.accountDataForm.controls;
  }

  public onSubmit() {

    if(this.form.password.value === this.form.checkpassword.value) {
      console.log("submit");
      this.showRequestMessage('success','Hasło zostało zmienione');
    } else {
      console.log("serror");
      this.showRequestMessage('error','','Coś poszło nie tak');
    }
  }

  public deleteAccount() {
    console.log("delete");
  }
}
