import { Component, OnInit } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { storage_Avaliable } from 'src/app/classes/storage_checker';
import { voivodeships } from 'src/app/classes/Voivodeship';

@Component({
  selector: 'app-company',
  templateUrl: './company.component.html',
  styleUrls: ['./company.component.scss']
})
export class CompanyComponent implements OnInit {
  public havePersonalData = new BehaviorSubject(false);
  public canShowAddForm = new BehaviorSubject(false);
  public canShowWorkForm = new BehaviorSubject(false);
  //dodać formularze do firmy/zakładu
  //funkcja do dodawania wielu zakładów dla jednej firmy
  //edycja/usuwanie firmy/zakładu
  //profil zakładu-wyświetlanie oddzielny komponent wraz do wyszukiwarki
  public _voivodeships = voivodeships;
  constructor() {}

  ngOnInit() {
    this.checkForPersonalData();
  }

  public addAnotherWork() {
    console.log('kolejny form');
  }
  public onSubmit(event: Event) {
    event.preventDefault();
    console.log('submit');
  }

  private checkForPersonalData() {
    if (
      storage_Avaliable('localStorage') &&
      localStorage.getItem('naturalUserData')
    ) {
      this.havePersonalData.next(true);
      console.log('są dane');
    } else {
      this.havePersonalData.next(false);
    }
  }

  public toggleAddForm() {
    this.canShowWorkForm.next(false);
    this.canShowAddForm.next(!this.canShowAddForm.value);
  }
  
  public toggleWorkForm() {
    this.canShowWorkForm.next(!this.canShowWorkForm.value);
  }

  public canShowDataList() {
    if (!this.canShowAddForm.value && !this.canShowWorkForm.value) {
      return true;
    } else {
      return false;
    }
  }

  public canShowAddsForm() {
    if (this.canShowAddForm.value && !this.canShowWorkForm.value) {
      return true;
    } else {
      return false;
    }
  }
}
