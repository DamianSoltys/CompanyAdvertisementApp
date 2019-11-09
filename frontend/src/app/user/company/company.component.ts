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
import { ActivatedRoute, Route, Router } from '@angular/router';
import { BranchService } from 'src/app/services/branch.service';

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
  backId:number;
  logoKey?:string;
  logoURL?:string;
}
@Component({
  selector: 'app-company',
  templateUrl: './company.component.html',
  styleUrls: ['./company.component.scss']
})
export class CompanyComponent implements OnInit {
  public voivodeshipOptions:string[] = voivodeships;
  public categoryOptions:string[] = categories;
  public selectConfig = {
    height: '300px',
  }
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
    addWork: false,
    backId:null,
  };
  public _voivodeships = voivodeships;
  public _categories = categories;

  public companyForm = this.fb.group({
    description: ['',[Validators.required]],
    category: ['', [Validators.required]],
    name: ['', [Validators.required,Validators.pattern(new RegExp(/^[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]+$/))]],
    nip: ['', [Validators.required,Validators.pattern(new RegExp(/^[0-9]+$/))]],
    regon: ['', [Validators.required,Validators.pattern(new RegExp(/^[0-9]+$/))]],
    companyWebsiteUrl: [''],
    address: this.fb.group({
      apartmentNo: ['', [Validators.required, Validators.pattern(new RegExp(/^[0-9A-Za-z]+$/))]],
      buildingNo: ['', [Validators.required, Validators.pattern(new RegExp(/^[0-9A-Za-z]+$/))]],
      city: ['', [Validators.required,Validators.pattern(new RegExp(/^[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]+$/))]],
      street: ['', [Validators.required,Validators.pattern(new RegExp(/^[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]+$/))]],
      voivodeship: ['', [Validators.required]]
    })
  });

  public workForm = this.fb.group({
    address: this.fb.group({
      apartmentNo: ['', [Validators.required,Validators.pattern(new RegExp(/^[0-9A-Za-z]+$/))]],
      buildingNo: ['', [Validators.required,Validators.pattern(new RegExp(/^[0-9A-Za-z]+$/))]],
      city: ['', [Validators.required,Validators.pattern(new RegExp(/^[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]+$/))]],
      street: ['', [Validators.required,Validators.pattern(new RegExp(/^[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]+$/))]],
      voivodeship: ['', [Validators.required]]
    }),
    geoX: [''],
    geoY: [''],
    name: ['', [Validators.required,Validators.pattern(new RegExp(/^[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]+$/))]]
  });

  public config = {
    toolbar: [['bold', 'italic', 'underline']]
  };

  constructor(
    private fb: FormBuilder,
    private pDataService: PersonalDataService,
    private cDataService: CompanyService,
    private uDataService: UserService,
    private bDataService:BranchService,
    private snackbarService:SnackbarService,
    private formErrorService:FormErrorService,
    private activatedRoute:ActivatedRoute,
    private router:Router,

  ) {}

  ngOnInit() {
    this.activatedRoute.queryParams.subscribe(params=>{
      this.editRequestData = <EditRequestData>params;
      console.log(this.editRequestData);
    });
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
    this.cDataService.getCompanyData.subscribe((data)=>{  
        this.getCompanyList(data);     
    });
  }

  private getStorageList() {
    if(storage_Avaliable('localStorage')) {
      let companyData:GetCompany[] = JSON.parse(localStorage.getItem('companyData'));
      console.log(companyData)
      if(companyData) {
        this.companyList = companyData;
      } else {
        this.companyList = undefined;
      }
    }
  }
  
  private getCompanyList(clearDataStorage?:boolean) {
    let subject = new Subject<any>();
    this.dataLoaded.next(false);
    subject.subscribe(()=>{
      this.dataLoaded.next(true);
    });

    if (storage_Avaliable('localStorage')) {
       if(clearDataStorage) {
         this.cDataService.deleteStorageData();
         this.companyList = undefined;
      } 
      let userREST: UserREST = JSON.parse(localStorage.getItem('userREST'));

      this.getStorageList();

   if(userREST.companiesIDs ) {
    if (userREST.companiesIDs.length) {
      this.companyList = [];
      userREST.companiesIDs.forEach((companyId,index) => {
        this.cDataService.getCompany(companyId).subscribe(
          response => {
            let companyData:GetCompany = <GetCompany>response.body;
            this.cDataService.getCompanyLogo(companyData).subscribe(response=>{             
             if(response.status != 204) {
              let reader = new FileReader();
              reader.addEventListener("load", () => {
                    companyData.logo = reader.result;
                    this.cDataService.storeCompanyData(companyData);
                    this.getStorageList();     
                    subject.next(true);    
              }, false);

              if (response.body) {
                  reader.readAsDataURL(response.body);
              }
             } else {
                companyData.logo = this.cDataService.defaultCListUrl;              
                this.cDataService.storeCompanyData(companyData); 
                this.getStorageList();                         
                subject.next(true);
             }
              
            },error=>{
              console.log('coś poszło nie tak');
              this.companyList=undefined;                        
              subject.next(true);
            });
          },
          error => {
            subject.next(true);
            console.log(error);
          }
        );
      });
    } else{
      console.log('nie ma firm')
      subject.next(true);
    }
   } else {
    console.log('Firmy pobrane ze storage')
    subject.next(true);
   }
      
    }
  }

  public checkForDoubles() {
    
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
    let branches:Branch[] = this.workForms;
    this.bDataService.addBranches(this.editRequestData.backId,branches,this.LogoList).subscribe(response=>{
      if(response) {
      this.snackbarService.open({
        message:'Pomyślnie dodano nowe zakłady',
        snackbarType:SnackbarType.success,
      });
      this.uDataService.updateUser().subscribe(data=>{
        this.workForms = undefined;
        this.cDataService.getCompanyData.next(true);
        this.bDataService.getBranchData.next(true);
        this.router.navigate(['companyProfile',this.editRequestData.backId]); 
      });
    } else {
      this.formErrorService.open({
        message:'Nie udało się dodać danych!'
      });
      this.setDefaultValues();
    }
    },error=>{
      this.formErrorService.open({
        message:'Nie udało się dodać danych!',
      });
      this.setDefaultValues();
    });
  }

  private patchCompanyData() {
    let companyData: Company;
    companyData = this.companyForm.value;

    this.cDataService
      .editCompany(companyData, this.editRequestData,this.companyLogo)
      .subscribe(
        response => {
          if(response) {
          this.snackbarService.open({
            message:'Dane firmy uległy edycji',
            snackbarType:SnackbarType.success,
          });
          this.getCompanyList(true);
          this.uDataService.updateUser().subscribe(data=>{
            this.cDataService.getCompanyData.next(true);
          this.router.navigate(['companyProfile',this.editRequestData.companyId]);      
          });
        } else {
          this.formErrorService.open({
            message:'Nie udało się zmienić danych!'
          });
          this.setDefaultValues();
        }
        },error=>{
          this.formErrorService.open({
            message:'Nie udało się zmienić danych!',
          });
          this.setDefaultValues();
        }
      );
  }

  private patchWorkIdData() {
    let branch:Branch = this.workForm.value;
    this.bDataService.editBranch(this.editRequestData,branch,this.workLogo).subscribe(response=>{
      if(response) {
      this.snackbarService.open({
        message:'Pomyślnie zaktualizowano zakład',
        snackbarType:SnackbarType.success,
      });
      this.uDataService.updateUser().subscribe(data=>{
        this.workForms = undefined;
        this.cDataService.getCompanyData.next(true);
        this.bDataService.getBranchData.next(true);
        this.router.navigate(['companyProfile',this.editRequestData.backId]); 
      });
    } else {
      this.formErrorService.open({
        message:'Nie udało się zmienić danych!'
      });
      this.setDefaultValues();
    }
    },error=>{
      this.formErrorService.open({
        message:'Nie udało się zmienić danych!',
      });
      this.setDefaultValues();
    })
  }
  

  private postData() {
    let companyData: Company;
    companyData = this.companyForm.value;
    companyData.branches = this.workForms;

    this.cDataService.addCompany(companyData,this.LogoList,this.companyLogo).subscribe(
      response => {
        if(response) {
        console.log(response)
        this.snackbarService.open({
          message:'Pomyślnie dodano firmę',
          snackbarType:SnackbarType.success,
        });
        this.uDataService.updateUser().subscribe(data=>{
           this.setDefaultValues();
           this.cDataService.getCompanyData.next(true);
           this.toggleDataList();
          // this.cDataService.deleteStorageData();
          // location.reload();
        });
      } else {
        this.formErrorService.open({
          message:'Nie udało się dodać firmy!',
        });
        this.setDefaultValues();
      }
      },error=>{
        this.formErrorService.open({
          message:'Nie udało się dodać firmy!',
        });
        this.setDefaultValues();
      } 
    );
  }

  public onFileSelected(event, companyForm: boolean) {
    if (!this.LogoList && !companyForm) {
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
    this.workForms = undefined;
    this.LogoList = undefined;
    this.companyList = undefined;
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

  public canShowBranchData() {
    if(this.workForms && (this.editRequestData.addWork || this.editRequestData.workId)) {
      return true;
    }else {
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
