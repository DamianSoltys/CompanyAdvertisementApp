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
  constructor(private fb: FormBuilder, private pdataService: PersonalDataService) { }

  ngOnInit() {
    this.personalDataForm = this.fb.group({
      firstName: [''],
     lastName: [''],
     voivodeship: [''],
     city: [''],
     street: [''],
     apartmentNo: [''],
     buildingNo: [''],
     phoneNo: ['']
    });
  }
  get form() {
    return this.personalDataForm.controls;
  }
  onSubmit() {
    this.pdataService.sendPersonalData(this.personalDataForm.value as PersonalData);
  }

}
