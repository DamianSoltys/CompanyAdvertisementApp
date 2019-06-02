import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { group } from '@angular/animations';

@Component({
  selector: 'app-personal-data',
  templateUrl: './personal-data.component.html',
  styleUrls: ['./personal-data.component.scss']
})
export class PersonalDataComponent implements OnInit {
  personalDataForm: FormGroup;
  constructor(private fb: FormBuilder) { }

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

}
