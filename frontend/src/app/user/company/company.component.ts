import { Component, OnInit, Input } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { storage_Avaliable } from 'src/app/classes/storage_checker';
import { voivodeships } from 'src/app/classes/Voivodeship';
import { categories } from 'src/app/classes/Category';
import { FormGroup, FormControl, FormBuilder, Validators } from '@angular/forms';
import { MouseEvent } from '@agm/core';
import { Company, Branch, GetCompany } from 'src/app/classes/Company';
import { PersonalDataService } from 'src/app/services/personal-data.service';
import { CompanyService } from 'src/app/services/company.service';
import { UserREST } from 'src/app/classes/User';
import { UserService } from 'src/app/services/user.service';
import { HttpEventType, HttpRequest, HttpResponse } from '@angular/common/http';

interface Position {
  latitude:number,
  longitude:number,
}
interface Marker {
  latitude:number,
  longitude:number,
  label:string,
}

interface EditRequestData {
  companyId:number;
  workId:number;
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
  private successMessageText = 'Akcja została zakończona pomyślnie';
  private errorMessageText = 'Akcja niepowiodła się';
  public companyList:GetCompany[];
  public successMessage: string = '';
  public errorMessage: string = '';
  public actualPosition:Position;
  public mapMarker:Marker;
  public workForms:Branch[];
  public workNumber:number = 1;
  private companyLogo:File;
  private workLogo:File;
  @Input() editRequestData:EditRequestData = {
    companyId:null,
    workId:null,
  };
  //dodać formularze do firmy/zakładu
  //funkcja do dodawania wielu zakładów dla jednej firmy
  //edycja/usuwanie firmy/zakładu
  //profil zakładu-wyświetlanie oddzielny komponent wraz do wyszukiwarki
  public _voivodeships = voivodeships;
  public _categories = categories;

  public companyForm = this.fb.group({
    logo:[null,[Validators.required]],
    description:['',[Validators.required]],
    category:['',[Validators.required]],
    name:['',[Validators.required]],
    nip:['',[Validators.required]],
    regon:['',[Validators.required]],
    companyWebsiteUrl:['',[Validators.required]],
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
      voivodeship:['',[Validators.required]],
    }),
    geoX:[''],
    geoY:[''],
    name:['',[Validators.required]],
    logo:[null,[Validators.required]],
  });

  config ={
    toolbar:[
      ['bold','italic','underline']
    ] 
  }

  constructor(private fb:FormBuilder,private pDataService:PersonalDataService,private cDataService:CompanyService,private uDataService:UserService) {}

  ngOnInit() {
    this.checkForPersonalData();
    this.getCompanyList();
    this.getActualPosition();
    this.showEditForm(); 
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
          this.showRequestMessage('error');
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
  private getCompanyList() {
    if(storage_Avaliable('localStorage')) {
      let userREST:UserREST = JSON.parse(localStorage.getItem('userREST'));
      if(userREST.companiesIDs) {
      this.companyList = [];
      userREST.companiesIDs.forEach(companyId=>{
       this.cDataService.getCompany(companyId).subscribe(response=>{
        this.companyList.push(<GetCompany>response.body);
        this.companyList.sort(this.companySort);       
       },error=>{
        console.log(error);
       });
      });
    }      
    }
  }

  public canShowCompanyList() {
    if(this.companyList && this.companyList.length !==0) {
      return true;
    } else {
      return false;
    }
  }

  private companySort(item1:GetCompany,item2:GetCompany) {
    return item1.companyId-item2.companyId;
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
      if(this.mapMarker){
        this._workForm.geoX.setValue(this.mapMarker.latitude);
        this._workForm.geoY.setValue(this.mapMarker.longitude);
        this.mapMarker = null;
      } else {
        this._workForm.geoX.setValue(this.actualPosition.latitude);
        this._workForm.geoY.setValue(this.actualPosition.longitude);
      }
      this.workNumber++;
      this.workForms.push(this.workForm.value);
      this.workForms[this.workForms.length-1].logo = this.workLogo;
      this.workForm.reset();
    }    
  }

  public onSubmit(event: Event) {
    event.preventDefault();
    if(this.workForm.valid) {
      this.addAnotherWork();
    }
    let companyData:Company;
    companyData = this.companyForm.value;
    companyData.branches = this.workForms;
    companyData.logo = this.companyLogo;
    console.log(companyData);

      this.cDataService.addCompany(companyData).subscribe(response=>{
        this.showRequestMessage('success');
        setTimeout(()=>{
          this.uDataService.updateUser();
          location.reload();
        },500);
      },error=>{
        this.showRequestMessage('error');
        this.setDefaultValues();
        console.log(error);
      });     
  }
  public onFileSelected(event,companyForm:boolean) {
    if(companyForm) {
      this.companyLogo = event.target.files[0];
    }else {
      this.workLogo = event.target.files[0];
    }
  }

  private showRequestMessage(
    type: string,
    successMessage: string = this.successMessageText,
    errorMessage: string = this.errorMessageText
  ) {
    if (type === 'success') {
      this.successMessage = successMessage;
      this.errorMessage = '';
    } else {
      this.successMessage = '';
      this.errorMessage = errorMessage;
    }
  }

  private setDefaultValues(){
      this.workForm.reset();
      this.companyForm.reset();
      this.workNumber = 1;
      this.workForms = null;
      this.toggleDataList();
  }

  private checkForPersonalData() {
    if (
      storage_Avaliable('localStorage') &&
      localStorage.getItem('naturalUserData')
    ) {
      this.havePersonalData.next(true);      
    } else {
      let userRest:UserREST = JSON.parse(localStorage.getItem('userREST'));

      if(userRest.naturalPersonID) {
        this.pDataService.getPersonalData(userRest.userID,userRest.naturalPersonID).subscribe(response=>{
          this.havePersonalData.next(true);
        },error=>{
          this.havePersonalData.next(false);
          this.showRequestMessage('error','','Aby dodać firmę musisz dodać dane osobowe!');
        });
      } else {
        this.havePersonalData.next(false);
        this.showRequestMessage('error','','Aby dodać firmę musisz dodać dane osobowe!');
      }
    }
  }

  public toggleAddForm() {
    this.canShowWorkForm.next(false);
    this.canShowAddForm.next(!this.canShowAddForm.value);
  }
  
  public toggleWorkForm() {
    this.canShowWorkForm.next(!this.canShowWorkForm.value);
  }

  public toggleDataList() {
    this.canShowAddForm.next(false);
    this.canShowWorkForm.next(false);
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

  public canShowRouteButton() {
    if(!this.canShowAddForm.value && !this.canShowWorkForm.value && !this.havePersonalData.value) {
      return true;
    }else{
      return false;
    }
  }

  private showEditForm() {
    if(this.editRequestData.companyId) {
      this.toggleAddForm();
    }else if(this.editRequestData.workId) {
      this.toggleWorkForm();
    }
  }
}
