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

import { PersonalData, UserREST } from 'src/app/classes/User';
import { PersonalDataService } from 'src/app/services/personal-data.service';
import { voivodeships } from 'src/app/classes/Voivodeship';
import { storage_Avaliable } from 'src/app/classes/storage_checker';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-personal-data',
  templateUrl: './personal-data.component.html',
  styleUrls: ['./personal-data.component.scss']
})
export class PersonalDataComponent implements OnInit {
  public personalDataForm: FormGroup;
  public successMessage: string = '';
  public errorMessage: string = '';
  public userObject: UserREST;
  public naturalUserDataObject: PersonalData;
  public _voivodeships = voivodeships;
  private successMessageText = 'Akcja została zakończona pomyślnie';
  private errorMessageText = 'Akcja niepowiodła się';

  public showData = new BehaviorSubject<boolean>(false);
  public showAddingForm = new BehaviorSubject<boolean>(false);
  public showEditingForm = new BehaviorSubject<boolean>(false);

  constructor(
    private fb: FormBuilder,
    private pdataService: PersonalDataService,
    private renderer: Renderer2,
    private userService: UserService
  ) {}

  ngOnInit() {
    this.personalDataForm = this.fb.group({
      address: this.fb.group({
        voivodeship: ['', [Validators.required]],
        city: [
          '',
          [
            Validators.required,
            Validators.pattern(new RegExp(/^[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]+$/))
          ]
        ],
        street: [
          '',
          [
            Validators.required,
            Validators.pattern(new RegExp(/^[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]+$/))
          ]
        ],
        apartmentNo: [
          '',
          [
            Validators.required,
            Validators.pattern(new RegExp(/^[0-9A-Za-z]+$/))
          ]
        ],
        buildingNo: [
          '',
          [
            Validators.required,
            Validators.pattern(new RegExp(/^[0-9A-Za-z]+$/))
          ]
        ]
      }),
      firstName: [
        '',
        [
          Validators.required,
          Validators.pattern(new RegExp(/^[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]+$/))
        ]
      ],
      lastName: [
        '',
        [
          Validators.required,
          Validators.pattern(new RegExp(/^[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]+$/))
        ]
      ],
      phoneNo: [
        '',
        [Validators.required, Validators.pattern(new RegExp(/^[0-9]+$/))]
      ]
    });
    this.getUserObject();
    this.checkForPersonalData();
  }

  private checkForPersonalData() {
    if (this.checkIfPersonalDataStorage()) {
      this.naturalUserDataObject = this.getPersonalDataStorage();
      this.showPersonalData();
    } else {
      this.getPersonalDataServer();
    }
  }

  private getUserObject() {
    this.userService.userREST.subscribe(data => {
      this.userObject = data;
    });
  }

  public showPersonalData(e?: Event) {
    if (e) {
      e.preventDefault();
    }
    this.showAddingForm.next(false);
    this.showEditingForm.next(false);
    this.showData.next(true);
  }

  public showEditForm() {
    this.showData.next(false);
    this.showAddingForm.next(false);
    this.showEditingForm.next(true);
  }
  public showAddForm() {
    this.showData.next(false);
    this.showAddingForm.next(true);
    this.showEditingForm.next(false);
  }

  private getPersonalDataServer() {
    if (this.userObject.naturalPersonID) {
      this.pdataService
        .getPersonalData(
          this.userObject.userID,
          this.userObject.naturalPersonID
        )
        .subscribe(
          response => {
            this.naturalUserDataObject = response.body as PersonalData;
            this.setStoragePersonalData(this.naturalUserDataObject);
            this.checkForPersonalData();
          },
          error => {
            this.naturalUserDataObject = {} as PersonalData;
            this.showAddForm();
          }
        );
    } else {
      this.naturalUserDataObject = {} as PersonalData;
      this.showAddForm();
    }
  }

  private showRequestMessage(
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

    setTimeout(() => {
      this.clearRequestMessage();
    }, 2000);
  }
  private clearRequestMessage() {
    this.successMessage = '';
    this.errorMessage = '';
  }

  private getPersonalDataStorage(): PersonalData {
    let naturalUserDataObject = {} as PersonalData;
    if (storage_Avaliable('localStorage')) {
      naturalUserDataObject = JSON.parse(
        localStorage.getItem('naturalUserData')
      );
      return naturalUserDataObject;
    }
  }

  private checkIfPersonalDataStorage() {
    if (
      storage_Avaliable('localStorage') &&
      localStorage.getItem('naturalUserData')
    ) {
      return true;
    } else {
      return false;
    }
  }

  private setStoragePersonalData(PersonalDataObject: PersonalData) {
    if (storage_Avaliable('localStorage')) {
      localStorage.setItem(
        'naturalUserData',
        JSON.stringify(PersonalDataObject)
      );
      this.pdataService.personalData.next(PersonalDataObject);
    }
  }

  private deleteStoragePersonalData() {
    if (storage_Avaliable('localStorage')) {
      localStorage.removeItem('naturalUserData');
      this.pdataService.personalData.next(null);
    }
  }

  get form() {
    return this.personalDataForm.controls;
  }

  get formAddress() {
    return this.personalDataForm.get('address')['controls'];
  }

  public onSubmit() {
    if (!this.showEditingForm.getValue()) {
      this.checkIfPostDataSuccess();
    } else {
      this.checkIfEditDataSuccess();
    }
  }

  private checkIfPostDataSuccess() {
    console.log(this.userObject);
    this.pdataService
      .sendPersonalData(
        this.personalDataForm.value as PersonalData,
        this.userObject.userID
      )
      .subscribe(
        (response: HttpResponse<any>) => {
          this.showRequestMessage('success', 'Dane zostały zapisane');
          this.setStoragePersonalData(this.personalDataForm.value);
          this.personalDataForm.reset();
          this.userService.updateUser();
          this.checkForPersonalData();
        },
        error => {
          console.log(error);
          this.showRequestMessage('error');
        }
      );
  }

  private checkIfEditDataSuccess() {
    this.pdataService
      .editPersonalData(
        this.personalDataForm.value as PersonalData,
        this.userObject.userID,
        this.userObject.naturalPersonID
      )
      .subscribe(
        (response: HttpResponse<any>) => {
          this.showRequestMessage('success', 'Dane uległy edycji');
          this.setStoragePersonalData(this.personalDataForm.value);
          this.personalDataForm.reset();
          this.deleteStoragePersonalData();
          this.userService.updateUser();
          this.getUserObject();
          this.checkForPersonalData();
        },
        error => {
          console.log(error);
          this.showRequestMessage('error');
        }
      );
  }

  public deleteData() {
    this.pdataService
      .deletePersonalData(
        this.userObject.userID,
        this.userObject.naturalPersonID
      )
      .subscribe(
        response => {
          this.showRequestMessage('success', 'Dane zostały usunięte');
          this.userService.updateUser();
          this.deleteStoragePersonalData();
          setTimeout(() => {
            this.showAddForm();
          }, 200);
        },
        error => {
          console.log(error);
          this.showRequestMessage('error', 'Nie udało się usunąć danych');
        }
      );
  }
}
