import { Component, OnInit, Renderer2 } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormControl,
  Validators,
  FormArray
} from '@angular/forms';
import { PersonalData, UserREST } from 'src/app/classes/User';
import { PersonalDataService } from 'src/app/services/personal-data.service';
import { voivodeships } from 'src/app/classes/Voivodeship';
import { storage_Avaliable } from 'src/app/classes/storage_checker';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-personal-data',
  templateUrl: './personal-data.component.html',
  styleUrls: ['./personal-data.component.scss']
})
export class PersonalDataComponent implements OnInit {
  personalDataForm: FormGroup;
  successMessage = '';
  errorMessage = '';
  userObject: UserREST;
  naturalUserDataObject: PersonalData;
  _voivodeships = voivodeships;

  showData: boolean;
  showAddingForm: boolean = false;
  constructor(
    private fb: FormBuilder,
    private pdataService: PersonalDataService,
    private renderer: Renderer2
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

    if (this.checkIfPersonalDataStorage()) {
      console.log('git');
      this.naturalUserDataObject = this.getPersonalDataStorage();
      this.showPersonalData(this.naturalUserDataObject);
    } else {
      this.getPersonalDataServer();
    }
  }

  selectOptionsRender() {
    let selectInput = this.renderer.selectRootElement('select');
    this._voivodeships.forEach(value => {
      let option = this.renderer.createElement('option');
      let text = this.renderer.createText(value);
      this.renderer.appendChild(option, text);
      this.renderer.appendChild(selectInput, option);
    });
  }

  showPersonalData(naturalUserData: PersonalData) {
    this.showData = true;
    console.log(this.getPersonalDataStorage());
  }

  renderPersonalData(naturalUserData: PersonalData) {
    if (this.showData) {
    }
  }

  getPersonalDataServer() {
    this.userObject = JSON.parse(localStorage.getItem('userREST'));
    this.pdataService
      .getPersonalData(this.userObject.userID, this.userObject.naturalPersonID)
      .subscribe(
        (response) => {
          this.naturalUserDataObject = response.body as PersonalData;
          if(response.status === 200) {
            console.log('Dane pobrane z servera');
            this.setStoragePersonalData(this.naturalUserDataObject);
            this.showPersonalData(this.naturalUserDataObject);
          } else {
            console.log('Coś poszło nie tak');
          }
        },
        error => {
          if(error.status === 404){
            this.showAddingForm = true;
            console.log(`User nie ma danych`);
          } else {
            this.naturalUserDataObject = {} as PersonalData;
            this.showAddingForm = false;
            console.log(`Wystąpił błąd:${error}`);
          }
        }
      );
  }

  getPersonalDataStorage(): PersonalData {
    let naturalUserDataObject = {} as PersonalData;
    if (storage_Avaliable('localStorage')) {
      naturalUserDataObject = JSON.parse(
        localStorage.getItem('naturalUserData')
      );
      return naturalUserDataObject;
    }
  }

  checkIfPersonalDataStorage() {
    if (
      storage_Avaliable('localStorage') &&
      JSON.parse(localStorage.getItem('naturalUserData'))
    ) {
      return true;
    } else {
      return false;
    }
  }

  setStoragePersonalData(PersonalDataObject: PersonalData) {
    if (
      storage_Avaliable('localStorage') &&
      !this.checkIfPersonalDataStorage()
    ) {
      localStorage.setItem(
        'naturalUserData',
        JSON.stringify(PersonalDataObject)
      );
      console.log('Dane zostały zapisane do magazynu!');
    } else {
      console.log('Zapisanie danych nie powiodło się, bądz są już zapisane');
    }
  }

  get form() {
    return this.personalDataForm.controls;
  }

  get formAddress() {
    return this.personalDataForm.get('address')['controls'];
  }

  onSubmit() {
    this.checkIfRequestSuccess();
  }

  checkIfRequestSuccess() {
    this.pdataService
      .sendPersonalData(
        this.personalDataForm.value as PersonalData,
        this.userObject.userID
      )
      .subscribe(
        (response: HttpResponse<any>) => {
          if (response.status === 200) {
            console.log(response);
            this.errorMessage = '';
            this.successMessage = 'Twoje dane zostały zapisane';
            this.setStoragePersonalData(this.personalDataForm.value);
            this.personalDataForm.reset();
          } else {
            console.log(`Wystąpił błąd:${response.status}`);
            this.errorMessage = 'Nie udało się zapisać danych';
          }
        },
        error => {
          console.log(error);
          this.successMessage = '';
          this.errorMessage = 'Nie udało się zapisać danych';
        }
      );
  }
}
