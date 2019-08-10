import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { PersonalData } from 'src/app/classes/User';
import { PersonalDataService } from 'src/app/services/personal-data.service';


@Component({
  selector: 'app-personal-data',
  templateUrl: './personal-data.component.html',
  styleUrls: ['./personal-data.component.scss']
})
export class PersonalDataComponent implements OnInit {
  personalDataForm: FormGroup;
  successMessage = '';
  errorMessage = '';
  constructor(private fb: FormBuilder, private pdataService: PersonalDataService) { }

  ngOnInit() {
    this.personalDataForm = this.fb.group({
     firstName: ['', [Validators.required, Validators.pattern(new RegExp(/^[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]+$/))]],
     lastName: ['', [Validators.required, Validators.pattern(new RegExp(/^[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]+$/))]],
     voivodeship: ['', [Validators.required, Validators.pattern(new RegExp(/^[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]+$/))]],
     city: ['', [Validators.required, Validators.pattern(new RegExp(/^[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]+$/))]],
     street: ['', [Validators.required, Validators.pattern(new RegExp(/^[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]+$/))]],
     apartmentNo: ['', [Validators.required, Validators.pattern(new RegExp(/^[0-9A-Za-z]+$/))]],
     buildingNo: ['', [Validators.required, Validators.pattern(new RegExp(/^[0-9A-Za-z]+$/))]],
     phoneNo: ['', [Validators.required, Validators.pattern(new RegExp(/^[0-9]+$/))]]
    });
  }
  get form() {
    return this.personalDataForm.controls;
  }
  onSubmit() {
    this.pdataService.sendPersonalData(this.personalDataForm.value as PersonalData).
    subscribe(response => {
      console.log(response);
      this.successMessage = 'Twoje dane zostały zapisane';
      this.personalDataForm.reset();
    }, error => {
      console.log(error);
      this.errorMessage = 'Nie udało się zapisać danych';
    });
  }

}
