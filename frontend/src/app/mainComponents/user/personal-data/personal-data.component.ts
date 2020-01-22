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

import { PersonalData, UserREST } from 'src/app/interfaces/User';
import { PersonalDataService } from 'src/app/services/personal-data.service';
import { voivodeships } from 'src/app/interfaces/Voivodeship';
import { storage_Avaliable } from 'src/app/interfaces/storage_checker';
import { UserService } from 'src/app/services/user.service';
import { SnackbarService, SnackbarType } from 'src/app/services/snackbar.service';
import { FormErrorService } from 'src/app/services/form-error.service';

@Component({
  selector: 'app-personal-data',
  templateUrl: './personal-data.component.html',
  styleUrls: ['./personal-data.component.scss']
})
export class PersonalDataComponent implements OnInit {
  public userObject: UserREST;
  public naturalUserDataObject: PersonalData;
  public _voivodeships = voivodeships;
  public showData = new BehaviorSubject<boolean>(false);
  public showAddingForm = new BehaviorSubject<boolean>(false);
  public showEditingForm = new BehaviorSubject<boolean>(false);
  public voivodeshipOptions: string[] = [
    'dolnośląskie',
    'kujawsko-pomorskie',
    'lubelskie',
    'lubuskie',
    'łódzkie',
    'małopolskie',
    'mazowieckie',
    'opolskie',
    'podkarpackie',
    'podlaskie',
    'pomorskie',
    'śląskie',
    'świętokrzyskie',
    'warmińsko-mazurskie',
    'wielkopolskie',
    'zachodniopomorskie'
  ];
  public selectConfig = {
    height: '300px',
  };
  public personalDataForm = this.fb.group({
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

  constructor(
    private fb: FormBuilder,
    private pdataService: PersonalDataService,
    private renderer: Renderer2,
    private userService: UserService,
    private snackbarService: SnackbarService,
    private formErrorService: FormErrorService
  ) { }

  ngOnInit() {
    this.getUserObject();
    this.checkForPersonalData();
  }

  get form() {
    return this.personalDataForm.controls;
  }

  get formAddress() {
    return this.personalDataForm.get('address')['controls'];
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
    this.setEditFormValues();
    this.showData.next(false);
    this.showAddingForm.next(false);
    this.showEditingForm.next(true);
  }
  public showAddForm() {
    this.showData.next(false);
    this.showAddingForm.next(true);
    this.showEditingForm.next(false);
  }

  public onSubmit() {
    if (!this.showEditingForm.getValue()) {
      this.checkIfPostDataSuccess();
    } else {
      this.checkIfEditDataSuccess();
    }
  }

  public deleteData() {
    this.pdataService.deletePersonalData(this.userObject.userID, this.userObject.naturalPersonID).subscribe(
      response => {
        this.snackbarService.open({
          message: 'Dane zostały usunięte',
          snackbarType: SnackbarType.success,
        });

        this.userService.updateUser();
        this.deleteStoragePersonalData();
        this.showAddForm();
      },
      error => {
        this.formErrorService.open({
          message: 'Nie udało się usunąć danych!',
        });
      }
    );
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

  private setEditFormValues() {
    this.formAddress.voivodeship.setValue(this.naturalUserDataObject.address.voivodeship);
    this.formAddress.apartmentNo.setValue(this.naturalUserDataObject.address.apartmentNo);
    this.formAddress.buildingNo.setValue(this.naturalUserDataObject.address.buildingNo);
    this.formAddress.city.setValue(this.naturalUserDataObject.address.city);
    this.formAddress.street.setValue(this.naturalUserDataObject.address.street);
    this.form.firstName.setValue(this.naturalUserDataObject.firstName);
    this.form.lastName.setValue(this.naturalUserDataObject.lastName);
    this.form.phoneNo.setValue(this.naturalUserDataObject.phoneNo);
  }

  private getPersonalDataServer() {
    if (this.userObject.naturalPersonID) {
      this.pdataService.getPersonalData(
        this.userObject.userID,
        this.userObject.naturalPersonID
      ).subscribe(
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

  private checkIfPostDataSuccess() {
    this.pdataService.sendPersonalData(
      this.personalDataForm.value as PersonalData,
      this.userObject.userID
    ).subscribe(
      (response: HttpResponse<any>) => {
        this.snackbarService.open({
          message: 'Dane zostały zapisane',
          snackbarType: SnackbarType.success,
        });

        this.setStoragePersonalData(this.personalDataForm.value);
        this.personalDataForm.reset();
        this.userService.updateUser().subscribe(() => {
          this.checkForPersonalData();
        });

      },
      error => {
        this.formErrorService.open({
          message: 'Nie udało się zapisać danych!',
        });
      }
    );
  }

  private checkIfEditDataSuccess() {
    this.pdataService.editPersonalData(
      this.personalDataForm.value as PersonalData,
      this.userObject.userID,
      this.userObject.naturalPersonID
    ).subscribe(
      (response: HttpResponse<any>) => {
        this.snackbarService.open({
          message: 'Dane uległy edycji',
          snackbarType: SnackbarType.success,
        });
        this.setStoragePersonalData(this.personalDataForm.value);
        this.personalDataForm.reset();
        this.deleteStoragePersonalData();
        this.userService.updateUser();
        this.getUserObject();
        this.checkForPersonalData();
      },
      error => {
        this.formErrorService.open({
          message: 'Nie udało się zmienić danych!',
        });
      }
    );
  }
}
