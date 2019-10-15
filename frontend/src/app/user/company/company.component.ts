import {
  Component,
  OnInit,
  Input,
  AfterViewInit,
  AfterViewChecked,
  ChangeDetectorRef,
  ViewChild,
  ElementRef
} from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';
import { storage_Avaliable } from 'src/app/classes/storage_checker';
import { voivodeships } from 'src/app/classes/Voivodeship';
import { categories } from 'src/app/classes/Category';
import {
  FormGroup,
  FormControl,
  FormBuilder,
  Validators
} from '@angular/forms';
import { MouseEvent } from '@agm/core';
import { Company, Branch, GetCompany } from 'src/app/classes/Company';
import { PersonalDataService } from 'src/app/services/personal-data.service';
import { CompanyService } from 'src/app/services/company.service';
import { UserREST } from 'src/app/classes/User';
import { UserService } from 'src/app/services/user.service';
import { HttpEventType, HttpRequest, HttpResponse } from '@angular/common/http';
import { LoaderService } from 'src/app/services/loader.service';
import { SnackbarService, SnackbarType } from 'src/app/services/snackbar.service';
import { FormErrorService } from 'src/app/services/form-error.service';

export interface Position {
  latitude: number;
  longitude: number;
}
export interface Marker {
  latitude: number;
  longitude: number;
  label: string;
}

export interface EditRequestData {
  companyId: number;
  workId: number;
  addWork: boolean;
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
  private dataLoaded = new BehaviorSubject(false);
  public companyList: GetCompany[];
  public actualPosition: Position = {
    latitude: 51.246452,
    longitude: 22.568445
  };
  public mapMarker: Marker;
  public workForms: Branch[];
  public workNumber: number = 1;
  private LogoList: File[];
  private companyLogo: File;
  private workLogo: File;
  @Input() editRequestData: EditRequestData = {
    companyId: null,
    workId: null,
    addWork: false
  };
  public _voivodeships = voivodeships;
  public _categories = categories;

  public companyForm = this.fb.group({
    description: ['', [Validators.required]],
    category: ['', [Validators.required]],
    name: ['', [Validators.required]],
    nip: ['', [Validators.required]],
    regon: ['', [Validators.required]],
    companyWebsiteUrl: ['', [Validators.required]],
    address: this.fb.group({
      apartmentNo: ['', [Validators.required]],
      buildingNo: ['', [Validators.required]],
      city: ['', [Validators.required]],
      street: ['', [Validators.required]],
      voivodeship: ['', [Validators.required]]
    })
  });

  public workForm = this.fb.group({
    address: this.fb.group({
      apartmentNo: ['', [Validators.required]],
      buildingNo: ['', [Validators.required]],
      city: ['', [Validators.required]],
      street: ['', [Validators.required]],
      voivodeship: ['', [Validators.required]]
    }),
    geoX: [''],
    geoY: [''],
    name: ['', [Validators.required]]
  });

  config = {
    toolbar: [['bold', 'italic', 'underline']]
  };

  constructor(
    private fb: FormBuilder,
    private pDataService: PersonalDataService,
    private cDataService: CompanyService,
    private uDataService: UserService,
    private loaderService: LoaderService,
    private snackbarService:SnackbarService,
    private formErrorService:FormErrorService

  ) {}

  ngOnInit() {
    this.checkForPersonalData();
    this.getActualPosition();
    this.getCompanyList();
    this.showEditForm();
    this.registerGetCompanyListener();
  }

  private getActualPosition() {
    let navigatorObject = window.navigator;

    if (storage_Avaliable('localStorage')) {
      if (!localStorage.getItem('actualPosition') && !this.actualPosition) {
        navigatorObject.geolocation.getCurrentPosition(
          position => {
            this.actualPosition = {
              latitude: position.coords.latitude,
              longitude: position.coords.longitude
            };
            localStorage.setItem(
              'actualPosition',
              JSON.stringify(this.actualPosition)
            );
          },
          error => {}
        );
      } else {
        let position = JSON.parse(localStorage.getItem('actualPosition'));

        if (position) {
          this.actualPosition = {
            latitude: position.latitude,
            longitude: position.longitude
          };
        }
      }
    }
  }

  private registerGetCompanyListener() {
    this.cDataService.getCompanyData.subscribe(()=>{
      this.getCompanyList();
    });
  }
  
  private getCompanyList() {
    if (storage_Avaliable('localStorage')) {
      let userREST: UserREST = JSON.parse(localStorage.getItem('userREST'));
      if (userREST.companiesIDs) {
        this.companyList = [];
        userREST.companiesIDs.forEach((companyId, index) => {
          this.cDataService.getCompany(companyId).subscribe(
            response => {
              this.companyList.push(<GetCompany>response.body);
              this.companyList.sort(this.companySort);
              this.cDataService.storeCompanyData(<GetCompany>response.body);
              if (index === userREST.companiesIDs.length) {
                this.dataLoaded.next(true);
              }
            },
            error => {
              console.log(error);
            }
          );
        });
      }
      if (
        !userREST.companiesIDs ||
        (userREST.companiesIDs && !userREST.companiesIDs.length)
      ) {
        this.dataLoaded.next(true);
      }
    }
  }
  private companySort(item1: GetCompany, item2: GetCompany) {
    return item1.companyId - item2.companyId;
  }

  public mapClickEvent($event: MouseEvent) {
    this.mapMarker = {
      latitude: $event.coords.lat,
      longitude: $event.coords.lng,
      label: 'Pozycja zakładu'
    };
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
    if (!this.workForms) {
      this.workForms = [];
    }
    if (this.workForm) {
      if (this.mapMarker) {
        this._workForm.geoX.setValue(this.mapMarker.latitude);
        this._workForm.geoY.setValue(this.mapMarker.longitude);
        this.mapMarker = null;
      } else {
        this._workForm.geoX.setValue(this.actualPosition.latitude);
        this._workForm.geoY.setValue(this.actualPosition.longitude);
      }
      this.workNumber++;
      this.workForms.push(this.workForm.value);
      if (this.workLogo) {
        this.LogoList.push(this.workLogo);
      }
      this.workForm.reset();
      if (!this.editRequestData.addWork) {
        this.toggleWorkForm();
      }
    }
  }

  public onSubmit(event: Event) {
    event.preventDefault();
    if (this.editRequestData.companyId) {
      this.patchCompanyData();
    } else if (this.editRequestData.workId) {
      this.patchWorkIdData();
    } else if (this.editRequestData.addWork) {
      this.addWorks();
    } else {
      this.postData();
    }
  }

  private addWorks() {
    if (this.workForm.valid) {
      this.addAnotherWork();
    }
    console.log('Dodawanko');
  }

  private patchCompanyData() {
    let companyData: Company;
    companyData = this.companyForm.value;

    this.cDataService
      .editCompany(companyData, this.editRequestData.companyId)
      .subscribe(
        response => {
          // this.cDataService.putFile('',this.companyLogo).subscribe(response=>{

          // },error=>{

          // });
          this.snackbarService.open({
            message:'Dane firmy uległy edycji',
            snackbarType:SnackbarType.success,
          });
          this.uDataService.updateUser().subscribe(data=>{
            this.getCompanyList();
          });
        },
        error => {
          this.formErrorService.open({
            message:'Nie udało się zmienić danych!'
          });
        }
      );
  }

  private patchWorkIdData() {
    console.log('patchWorkID');
  }

  private postData() {
    let companyData: Company;
    companyData = this.companyForm.value;
    companyData.branches = this.workForms;
    if(this.companyLogo) {
      this.LogoList.unshift(this.companyLogo);
    }
    let file:File = this.LogoList?this.LogoList[0]:undefined;
    this.cDataService.addCompany(companyData,file).subscribe(
      response => {
        // this.cDataService.putFile('',this.LogoList).subscribe(response=>{

        // },error=>{

        // });
        console.log(response);
        this.snackbarService.open({
          message:'Pomyślnie dodano firmę',
          snackbarType:SnackbarType.success,
        });
        this.uDataService.updateUser().subscribe(data=>{
          this.getCompanyList();
        });
      },
      error => {
        this.formErrorService.open({
          message:'Nie udało się dodać firmy!',
        });
        this.setDefaultValues();
      }
    );
  }

  public onFileSelected(event, companyForm: boolean) {
    if (!this.LogoList) {
      this.LogoList = [];
    }
    if (companyForm) {
      this.companyLogo = event.target.files[0];
    } else {
      this.workLogo = event.target.files[0];
    }
  }

  private setDefaultValues() {
    this.workForm.reset();
    this.companyForm.reset();
    this.workNumber = 1;
    this.workForms = null;
  }

  private checkForPersonalData() {
    if (
      storage_Avaliable('localStorage') &&
      localStorage.getItem('naturalUserData')
    ) {
      this.havePersonalData.next(true);
    } else {
      let userRest: UserREST = JSON.parse(localStorage.getItem('userREST'));

      if (userRest.naturalPersonID) {
        this.pDataService
          .getPersonalData(userRest.userID, userRest.naturalPersonID)
          .subscribe(
            response => {
              this.havePersonalData.next(true);
            },
            error => {
              this.havePersonalData.next(false);
              this.snackbarService.open({
                message:'Aby dodać firmę musisz dodać dane osobowe!',
                snackbarType:SnackbarType.error,
              });
            }
          );
      } else {
        this.havePersonalData.next(false);
        this.snackbarService.open({
          message:'Aby dodać firmę musisz dodać dane osobowe!',
          snackbarType:SnackbarType.error,
        });
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
    if (
      !this.canShowAddForm.value &&
      !this.canShowWorkForm.value &&
      !this.havePersonalData.value
    ) {
      return true;
    } else {
      return false;
    }
  }

  private showEditForm() {
    if (this.editRequestData.companyId) {
      this.toggleAddForm();
    } else if (this.editRequestData.workId || this.editRequestData.addWork) {
      this.toggleWorkForm();
    }
  }

  public canShowCompanyList() {
    if (this.companyList && this.companyList.length !== 0) {
      return true;
    } else {
      return false;
    }
  }

  public canShowText() {
    if (!this.canShowCompanyList() && this.dataLoaded.getValue()) {
      return true;
    } else {
      return false;
    }
  }

  public canShowBranchBackBtn() {
    if (!this.editRequestData.workId && !this.editRequestData.addWork) {
      return true;
    } else {
      return false;
    }
  }
}
