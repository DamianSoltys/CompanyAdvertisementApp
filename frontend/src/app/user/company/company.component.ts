import { Component, OnInit } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { storage_Avaliable } from 'src/app/classes/storage_checker';
import { voivodeships } from 'src/app/classes/Voivodeship';
import { FormGroup, FormControl, FormBuilder, Validators } from '@angular/forms';
import { MouseEvent } from '@agm/core';

interface Position {
  latitude:number,
  longitude:number,
}
interface Marker {
  latitude:number,
  longitude:number,
  label:string,
}
@Component({
  selector: 'app-company',
  templateUrl: './company.component.html',
  styleUrls: ['./company.component.scss']
})
export class CompanyComponent implements OnInit {
  public havePersonalData = new BehaviorSubject(false);
  public canShowAddForm = new BehaviorSubject(false);
  public canShowWorkForm = new BehaviorSubject(false);
  public actualPosition:Position;
  public mapMarker:Marker;
  public workForms:FormGroup[];
  //dodać formularze do firmy/zakładu
  //funkcja do dodawania wielu zakładów dla jednej firmy
  //edycja/usuwanie firmy/zakładu
  //profil zakładu-wyświetlanie oddzielny komponent wraz do wyszukiwarki
  public _voivodeships = voivodeships;

  public companyForm = this.fb.group({
    description:[''],
    category:['',[Validators.required]],
    name:['',[Validators.required]],
    nip:['',[Validators.required]],
    regon:['',[Validators.required]],
    url:['',[Validators.required]],
    address:this.fb.group({
      apartmentNo:['',[Validators.required]],
      buildingNo:['',[Validators.required]],
      city:['',[Validators.required]],
      street:['',[Validators.required]],
      voivodeship:['',[Validators.required]],
    }),
  });

  public workForm = this.fb.group({
    address:this.fb.group({
      apartmentNo:['',[Validators.required]],
      buildingNo:['',[Validators.required]],
      city:['',[Validators.required]],
      street:['',[Validators.required]],
      voivodeship:[''],
    }),
    geoX:[''],
    geoY:[''],
    name:['',[Validators.required]],
  });

  config ={
    toolbar:[
      ['bold','italic','underline']
    ] 
  }

  constructor(private fb:FormBuilder) {}

  ngOnInit() {
    this.checkForPersonalData();
    this.getActualPosition();
  }

  private getActualPosition() {
    let navigatorObject = window.navigator;

    if(storage_Avaliable('localStorage')) {     
      if(!localStorage.getItem('actualPosition') && !this.actualPosition){
        navigatorObject.geolocation.getCurrentPosition((position)=>{
           this.actualPosition = {
            latitude:position.coords.latitude,
            longitude:position.coords.longitude,
          };
          localStorage.setItem('actualPosition',JSON.stringify(this.actualPosition));          
        },error =>{
          console.log("Coś poszło nie tak !");
        })
      } else {
        let position = JSON.parse(localStorage.getItem('actualPosition'));

        this.actualPosition = {
          latitude:position.latitude,
          longitude:position.longitude,
        };
      }
      
    }
  }

  public mapClickEvent($event:MouseEvent) {
    this.mapMarker = {
      latitude:$event.coords.lat,
      longitude:$event.coords.lng,
      label:"Pozycja zakładu",
    }
  }

  public get _companyForm() {
    return this.companyForm.controls;
  }

  public get _companyAddress() {
    return this.companyForm.get('address')['controls'];
  }

  public get _workForm() {
    return this.workForm.controls;
  }
  public get _workAddress() {
    return this.workForm.get('address')['controls'];
  }

  public addAnotherWork() {
    if(!this.workForms) {
      this.workForms = [];
    }
    if(this.workForm) {
      this.workForms.push(this.workForm);
      this.workForm.reset();
      console.log(this.workForms);
    }    
  }

  public onSubmit(event: Event) {
    event.preventDefault();
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
