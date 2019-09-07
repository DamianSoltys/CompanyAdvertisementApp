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

// Public pierwsze/private ostatnie!
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
    private userService:UserService,
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
    this.userObject = JSON.parse(localStorage.getItem('userREST'));
    this.checkForPersonalData();
    this.updateUserObject();
  }

  private checkForPersonalData() {
    if (this.checkIfPersonalDataStorage()) {
      console.log('git');
      this.naturalUserDataObject = this.getPersonalDataStorage();
      
      setTimeout(()=>{
        this.showPersonalData();
      },2000);

      console.log(this.userObject);
    } else {
      this.getPersonalDataServer();
    }
  }

  public showPersonalData(e?:Event) {
    if(e) {
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
    if(this.userObject.naturalPersonID){
    this.pdataService
      .getPersonalData(this.userObject.userID, this.userObject.naturalPersonID)
      .subscribe(
        response => {
          this.naturalUserDataObject = response.body as PersonalData;
            console.log('Dane pobrane z servera');
            this.setStoragePersonalData(this.naturalUserDataObject);
            setTimeout(()=>{
              this.showPersonalData();
            },2000); 
        },
        error => {
          if (error.status === 404 || error.status === 400) {
            this.naturalUserDataObject = {} as PersonalData;
            this.showAddForm();
          } 
        }
      );
    } else {
      this.naturalUserDataObject = {} as PersonalData;
      this.showAddForm();
    }
  }

  private showRequestMessage(
    context: string,
    type: string,
    successMessage: string = this.successMessageText,
    errorMessage: string = this.errorMessageText
  ) {
    switch (context) {
      case 'get': {
        if (type === 'success') {
          this.successMessage = successMessage;
          this.errorMessage = '';
        } else {
          this.successMessage = '';
          this.errorMessage = errorMessage;
        }
        break;
      }
      case 'post': {
        if (type === 'success') {
          this.successMessage = successMessage;
          this.errorMessage = '';
        } else {
          this.successMessage = '';
          this.errorMessage = errorMessage;
        }
        break;
      }
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
    if (
      storage_Avaliable('localStorage')
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

  public onSubmit() {
    console.log(this.personalDataForm.value)
    if(!this.showEditingForm.getValue()){
      this.checkIfPostDataSuccess();
    } else {
      this.checkIfEditDataSuccess();
    }
    
  }

  updateUserObject() {
    this.userService.getActualUser(this.userObject.userID).subscribe(response =>{
      console.log(response);
      if (storage_Avaliable('localStorage')) {
        const userNewObject: UserREST = response.body;      
        localStorage.setItem('userREST', JSON.stringify(userNewObject));
      } else {
        console.log('Nie udało się zapisać nowych danych usera');
      }  
    },error =>{
      console.log(error);
    });
  }

  private checkIfPostDataSuccess() {
    this.pdataService
      .sendPersonalData(
        this.personalDataForm.value as PersonalData,
        this.userObject.userID
      )
      .subscribe(
        (response: HttpResponse<any>) => {
            console.log(response);
            this.showRequestMessage('post', 'success', 'Dane zostały zapisane');
            this.setStoragePersonalData(this.personalDataForm.value);
            this.personalDataForm.reset();
            this.naturalUserDataObject = this.personalDataForm.value;
            this.updateUserObject();           
            setTimeout(()=>{
              this.showPersonalData();
            },2000); 
        },
        error => {
          console.log(error);
          this.showRequestMessage('post', 'error');
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
          console.log(response);
          this.showRequestMessage('post', 'success', 'Dane zostały zapisane');
          this.setStoragePersonalData(this.personalDataForm.value);
          this.personalDataForm.reset();
          localStorage.removeItem('naturalUserData');
          this.checkForPersonalData();
          setTimeout(()=>{
            this.showPersonalData();
          },2000);      
      },
      error => {
        console.log(error);
        console.log(this.personalDataForm.value);
        this.showRequestMessage('post', 'error');
      }
    );
  }

  public deleteData() {
    console.log("usuwanie danych");
  }
}
