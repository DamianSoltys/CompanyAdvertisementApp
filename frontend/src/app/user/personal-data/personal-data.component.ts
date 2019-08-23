import { Component, OnInit,Renderer2 } from '@angular/core';
import { FormBuilder, FormGroup, FormControl, Validators, FormArray } from '@angular/forms';
import { PersonalData, UserREST } from 'src/app/classes/User';
import { PersonalDataService } from 'src/app/services/personal-data.service';
import { voivodeships } from 'src/app/classes/Voivodeship';


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
  _voivodeships = voivodeships;
  constructor(private fb: FormBuilder, private pdataService: PersonalDataService,private renderer:Renderer2) { }

  ngOnInit() {
    this.personalDataForm = this.fb.group({
     address:this.fb.group({
     voivodeship: ['', [Validators.required]],
     city: ['', [Validators.required, Validators.pattern(new RegExp(/^[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]+$/))]],
     street: ['', [Validators.required, Validators.pattern(new RegExp(/^[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]+$/))]],
     apartmentNo: ['', [Validators.required, Validators.pattern(new RegExp(/^[0-9A-Za-z]+$/))]],
     buildingNo: ['', [Validators.required, Validators.pattern(new RegExp(/^[0-9A-Za-z]+$/))]],
     }),
     firstName: ['', [Validators.required, Validators.pattern(new RegExp(/^[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]+$/))]],
     lastName: ['', [Validators.required, Validators.pattern(new RegExp(/^[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]+$/))]],
     phoneNo: ['', [Validators.required, Validators.pattern(new RegExp(/^[0-9]+$/))]]
    });

    this.selectOptionsRender();
    //TODO
    this.userObject = JSON.parse(localStorage.getItem('userREST'));
    this.pdataService.getPersonalData(this.userObject.userID,this.userObject.naturalPersonID).subscribe(response=>{
      console.log(response);
    },error=>{
      console.log(error);
    });
  }

  selectOptionsRender() {
    let selectInput = this.renderer.selectRootElement('select');
    this._voivodeships.forEach(value=>{
    let option = this.renderer.createElement('option');
    let text = this.renderer.createText(value);
    this.renderer.appendChild(option,text);
    this.renderer.appendChild(selectInput,option);
    });
  }


  get form() {
    return this.personalDataForm.controls;
  }

  get formAddress() {
    return this.personalDataForm.get('address')['controls'];
  }

  onSubmit() {
    this.pdataService.sendPersonalData(this.personalDataForm.value as PersonalData,this.userObject.userID).
    subscribe(response => {
      console.log(response);
      this.errorMessage='';
      this.successMessage = 'Twoje dane zostały zapisane';
      this.personalDataForm.reset();
    }, error => {
      console.log(error);
      this.successMessage='';
      this.errorMessage = 'Nie udało się zapisać danych';
    });
  }

}
