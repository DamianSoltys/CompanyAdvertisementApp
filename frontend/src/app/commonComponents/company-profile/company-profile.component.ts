import {
  Component,
  OnInit,
  ViewChild,
  ElementRef,
  AfterViewInit
} from '@angular/core';
import { Router, ParamMap, ActivatedRoute } from '@angular/router';
import { GetCompany, Branch, ConnectionStatus, MediaTypeEnum } from 'src/app/classes/Company';
import { CompanyService } from 'src/app/services/company.service';
import { storage_Avaliable } from 'src/app/classes/storage_checker';
import { BehaviorSubject, Subject } from 'rxjs';
import { BranchService } from 'src/app/services/branch.service';
import { EditRequestData } from 'src/app/user/company/company.component';
import { UserREST } from 'src/app/classes/User';
import { SnackbarService, SnackbarType } from 'src/app/services/snackbar.service';
import { FormErrorService } from 'src/app/services/form-error.service';
import { FormBuilder, Validators } from '@angular/forms';
import { NewsletterService } from 'src/app/services/newsletter.service';
import { LoginService } from 'src/app/services/login.service';

@Component({
  selector: 'app-company-profile',
  templateUrl: './company-profile.component.html',
  styleUrls: ['./company-profile.component.scss']
})
export class CompanyProfileComponent implements OnInit,AfterViewInit {
  public editData: EditRequestData;
  public paramId: number;
  public owner = new BehaviorSubject(false);
  public companyData: GetCompany;
  public branchData: Branch[];
  public userREST: UserREST;
  public isLoaded = new BehaviorSubject(false);
  public canShowBranches = new BehaviorSubject(false);
  public canShowEditForm = new BehaviorSubject(false);
  public canShowAddBranchForm = new BehaviorSubject(false);
  public canShowCompany = new BehaviorSubject(true);
  public isNewsletter = new BehaviorSubject(false);
  public isFacebookConnected = false;
  public otherFBAccount = false;
  public isTwitterConnected = false;
  @ViewChild('checkLabel') label:ElementRef;

  public newsletterFormUser = this.fb.group({
    isChecked:['',[Validators.required]],
  });
  public newsletterFormGuest = this.fb.group({
    email:['',[Validators.required]],
  });
  constructor(
    private activatedRoute: ActivatedRoute,
    private cDataService: CompanyService,
    private router: Router,
    private bDataService: BranchService,
    private snackbarService:SnackbarService,
    private formErrorService:FormErrorService,
    private fb:FormBuilder,
    private nDataService:NewsletterService,
    private lgService:LoginService
  ) {}

  ngOnInit() {
    this.activatedRoute.params.subscribe(params => {
      this.paramId = params['id'];
    });
    this.getCompanyData();
    this.registerListeners();
    this.checkMediaConnection();
  }

  ngAfterViewInit() {
    if(this.label) {
      this.label.nativeElement.addEventListener('click',()=>{
        if(!this.newsletterFormUser.controls.isChecked.value) {
          this.newsletterFormUser.controls.isChecked.setValue(true)
        } else {
          this.newsletterFormUser.controls.isChecked.setValue(false)
        }
      });
    }
  }

  private registerListeners() {
    this.bDataService.getBranchData.subscribe(()=>{
      this.getCompanyData(true);
    });
    this.lgService.FBstatus.subscribe(()=>{
      this.getCompanyData(true);
    });
    this.lgService.FBConnection.subscribe(data=>{
      if(data) {
        this.checkFbConnection();
      }
    });
  }

  private checkMediaConnection() {
    this.lgService.checkIfUserFBLogged();
  }

  private getCompanyData(clearCompanyStorage?:boolean) {
    if(clearCompanyStorage) {
      this.cDataService.deleteStorageData();
      this.companyData = undefined;
    }
    
    this.getStorageCompanyData();
    console.log(this.companyData);

    if (!this.companyData) {
      this.cDataService.getCompany(this.paramId).subscribe(
        response => {
          this.companyData = <GetCompany>response.body;
          console.log(this.companyData)
          this.cDataService.getCompanyLogo(this.companyData).subscribe(response=>{
            if(response.status != 204) {
              let reader = new FileReader();
                reader.addEventListener("load", () => {
                    this.companyData.logo = reader.result;
                    this.cDataService.storeCompanyData(this.companyData);
                    this.checkForCompanyOwnership();
                    this.getBranchData();
                }, false);

                if (response.body) {
                    reader.readAsDataURL(response.body);
                }
            } else {
            this.companyData.logo = this.cDataService.defaultCProfileUrl;
            this.cDataService.storeCompanyData(this.companyData);
            this.checkForCompanyOwnership();
            this.getBranchData();
            }
          },error=>{
            this.companyData.logo = this.cDataService.defaultCProfileUrl;
            this.cDataService.storeCompanyData(this.companyData);
            this.checkForCompanyOwnership();
            this.getBranchData();
          });
        },
        error => {
          this.checkForCompanyOwnership();
          this.snackbarService.open({
            message:'Nie udało się załadować danych!',
            snackbarType:SnackbarType.error,
          });
        }
      );
    }
  }

  private getStorageCompanyData() {
    if (storage_Avaliable('localStorage')) {
      let companyData: GetCompany[] = JSON.parse(
        localStorage.getItem('companyData')
      );
      if(companyData) {
        companyData.forEach(company => {
          if (this.paramId == company.companyId) {
            this.companyData = company;
            this.checkForCompanyOwnership();
            this.getBranchData();
          }
        });
      }
    }
  }

  private checkForCompanyOwnership() {
    if (
      storage_Avaliable('localStorage') &&
      JSON.parse(localStorage.getItem('userREST'))
    ) {
      this.userREST = JSON.parse(localStorage.getItem('userREST'));
      this.checkNewsletterSubscription();
      if(this.userREST.companiesIDs) {
        this.userREST.companiesIDs.forEach(companyId => {
          if (this.companyData && this.companyData.companyId === companyId) {
            this.owner.next(true);
          }
        });
      }
    }
  }

  private getBranchData() {
    let subject = new Subject<any>();
    subject.subscribe(()=>{
      this.isLoaded.next(true);
    });

    if (this.companyData) {
      this.branchData = [];
      console.log(this.companyData.branchesIDs)

      if(!this.companyData.branchesIDs.length) {
        subject.next(true);
      }

      this.companyData.branchesIDs.forEach((branchId, index) => {       
          this.bDataService.getBranch(branchId).subscribe(
            response => { 
              let branchData: Branch = <Branch>response.body;             
              this.bDataService.getBranchLogo(branchData).subscribe(response=>{
                if(response.status != 204) {
                  let reader = new FileReader();
                  reader.addEventListener("load", () => {
                    console.log('elo')
                    branchData.logo = reader.result;
                    branchData.branchId = branchId;
                    this.branchData.push(branchData);
                    this.bDataService.storeBranchData(branchData);
                    subject.next(true);
                    
                }, false);

                if (response.body) {
                    reader.readAsDataURL(response.body);
                }
                } else {
                  branchData.logo = this.bDataService.defaultLogoUrl;
                branchData.branchId = branchId;
                this.branchData.push(branchData);
                this.bDataService.storeBranchData(branchData);
                subject.next(true);
                }

              },error=>{
                branchData.logo = this.bDataService.defaultLogoUrl;
                branchData.branchId = branchId;
                this.branchData.push(branchData);
                this.bDataService.storeBranchData(branchData);
                subject.next(true);
              });

            },
            error => {
              this.snackbarService.open({
                message:'Nie udało się załadować danych!',
                snackbarType:SnackbarType.error,
              });
              subject.next(false);
            }
          );      
      });
    }
    
  }

  public facebookLogin($event) {
    event.preventDefault();
    this.lgService.facebookLogin(this.companyData.companyId).subscribe(response=>{
      console.log(response);
    });
  }

  public twitterLogin($event) {

    event.preventDefault();
    this.lgService.twitterLogin().subscribe(response=>{
      console.log(response);
    });
  }

  public checkNewsletterSubscription() {
    this.nDataService.getSubscriptionStatus(this.companyData.companyId,this.userREST.userID).subscribe(response=>{
      this.isNewsletter.next(<boolean>response.body);
    },error=>{
      console.log(error);
    });
  }

  public saveToNewsletter($event) {
    event.preventDefault();

    if(this.newsletterFormGuest.valid) {
      this.nDataService.saveToNewsletter(this.newsletterFormGuest.controls.email.value,this.companyData.companyId).subscribe(response=>{
        if(this.userREST) {
          this.snackbarService.open({
            message:'Zostałeś zapisany na newsletter!',
            snackbarType:SnackbarType.success,
          });
        } else {
          this.snackbarService.open({
            message:'Proszę potwierdzić zapis do newslettera linkiem wysłanym na podany adres!',
            snackbarType:SnackbarType.success,
          });
        }
        this.checkNewsletterSubscription();
      },error=>{
        console.log(error);
        this.formErrorService.open({
          message:'Nie udało się zapisać do newslettera',
        });
      })

    }else {
      this.nDataService.saveToNewsletter(this.userREST.emailAddress,this.companyData.companyId).subscribe(response=>{
        this.snackbarService.open({
          message:'Proszę potwierdzić zapis do newslettera linkiem wysłanym na podany adres!',
          snackbarType:SnackbarType.success,
        });
      },error=>{
        console.log(error);
        this.formErrorService.open({
          message:'Nie udało się zapisać do newslettera',
        });
      })
    }
  }

  public goBack() {
    this.canShowBranches.next(false);
    this.canShowEditForm.next(false);
    this.canShowAddBranchForm.next(false);
    this.canShowCompany.next(true);
  }

  public goToFBProfile($event) {
    this.companyData.socialProfileConnectionDtos.forEach(connection=>{
      if(connection.socialPlatform === MediaTypeEnum.FB) {
        window.open(connection.connectionStatus.profileURL, "_blank");
      }
    });
  }

  public showBranches() {
    this.canShowCompany.next(false);
    this.canShowBranches.next(true);
  }

  public showAddBranchform() {
    this.editData = {
      companyId: null,
      workId: null,
      addWork: true,
      backId:this.companyData.companyId,
    };
    this.router.navigate(['branchEdit'],{relativeTo:this.activatedRoute,queryParams:this.editData});
  }

  public showEditForm() {
    this.editData = {
      companyId: this.companyData.companyId,
      workId: null,
      addWork: false,
      backId:null,
      logoKey:this.companyData.logoKey?this.companyData.logoKey:undefined,
      putLogoUrl:this.companyData.putLogoURL?this.companyData.putLogoURL:undefined,
      getLogoUrl:this.companyData.getLogoURL?this.companyData.getLogoURL:undefined,
    };
    //this.canShowEditForm.next(true);
    //this.canShowCompany.next(false);
    this.router.navigate(['companyEdit'],{relativeTo:this.activatedRoute,queryParams:this.editData});
  }

  public showEmptyBranchData() {
    if(this.branchData && this.isLoaded.value) {
      if(this.branchData.length) {
        return false;
      } else {
        return true;
      }
    } else {
      return false;
    }
  }

  public showNewsletterForm() {
    if(storage_Avaliable('localStorage')) {
      if(this.userREST && !this.owner.value && !this.isNewsletter.value) {
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  public checkFbConnection() {
    if(this.companyData) {
      if(this.companyData.socialProfileConnectionDtos) {
        this.companyData.socialProfileConnectionDtos.forEach(connection=>{
          if(connection.connectionStatus.status === ConnectionStatus.Connected){
            if(connection.socialPlatform === MediaTypeEnum.FB) {
              this.isFacebookConnected = true;
            }
          } else {
            if(connection.socialPlatform === MediaTypeEnum.FB) {
              this.otherFBAccount = true;
            }
          }
        });
      } else {
        this.isFacebookConnected = false;
      }
    }
  }

  public showNewsletterFormContainer() {
    if(this.isLoaded.value && !this.owner.value) {
      return true;
    } else {
      return false;
    }
  }
  
}
