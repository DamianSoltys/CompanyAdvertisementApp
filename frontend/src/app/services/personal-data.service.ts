import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { PersonalData } from '../classes/User';

@Injectable({
  providedIn: 'root'
})
export class PersonalDataService {

  constructor() { }

  sendPersonalData(personalData: PersonalData) {
    console.log(personalData);
  }
}
