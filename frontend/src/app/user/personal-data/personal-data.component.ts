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

@Component({
  selector: 'app-personal-data',
  templateUrl: './personal-data.component.html',
  styleUrls: ['./personal-data.component.scss']
})

// Public pierwsze/private ostatnie!
export class PersonalDataComponent implements OnInit {
  public personalDataForm: FormGroup;
  public successMessage: string = '';
  public errorMessage: string = '';
  public userObject: UserREST;
  public naturalUserDataObject: PersonalData;
  public _voivodeships = voivodeships;

  public showData = new BehaviorSubject<boolean>(false);
  public showAddingForm = new BehaviorSubject<boolean>(false);

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

  // public selectOptionsRender(): void {
  //   let selectInput = this.renderer.selectRootElement('select');
  //   this._voivodeships.forEach(value => {
  //     let option = this.renderer.createElement('option');
  //     let text = this.renderer.createText(value);
  //     this.renderer.appendChild(option, text);
  //     this.renderer.appendChild(selectInput, option);
  //   });
  // }

  showPersonalData(naturalUserData: PersonalData) {
    this.showData.next(true);
  }

  showEditForm() {
    this.showData.next(false);
    this.showAddingForm.next(true);
  }

  getPersonalDataServer() {
    this.userObject = JSON.parse(localStorage.getItem('userREST'));
    this.pdataService
      .getPersonalData(this.userObject.userID, this.userObject.naturalPersonID)
      .subscribe(
        response => {
          this.naturalUserDataObject = response.body as PersonalData;
          if (response.status === 200) {
            console.log('Dane pobrane z servera');
            this.setStoragePersonalData(this.naturalUserDataObject);
            this.showPersonalData(this.naturalUserDataObject);
          } else {
            console.log('Coś poszło nie tak');
          }
        },
        error => {
          if (error.status === 404) {
            this.showAddingForm.next(true);
            console.log(`User nie ma danych`);
          } else {
            this.naturalUserDataObject = {} as PersonalData;
            this.showAddingForm.next(false);
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
