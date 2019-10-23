import {
  Component,
  OnInit,
  ViewChild,
  ElementRef,
  AfterViewInit
} from '@angular/core';
import { Router, ParamMap, ActivatedRoute } from '@angular/router';
import { GetCompany, Branch } from 'src/app/classes/Company';
import { CompanyService } from 'src/app/services/company.service';
import { storage_Avaliable } from 'src/app/classes/storage_checker';
import { BehaviorSubject, Subject } from 'rxjs';
import { BranchService } from 'src/app/services/branch.service';
import { EditRequestData } from 'src/app/user/company/company.component';
import { UserREST } from 'src/app/classes/User';
import { SnackbarService, SnackbarType } from 'src/app/services/snackbar.service';
import { FormErrorService } from 'src/app/services/form-error.service';

@Component({
  selector: 'app-company-profile',
  templateUrl: './company-profile.component.html',
  styleUrls: ['./company-profile.component.scss']
})
export class CompanyProfileComponent implements OnInit {
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

  constructor(
    private activatedRoute: ActivatedRoute,
    private cDataService: CompanyService,
    private router: Router,
    private bDataService: BranchService,
    private snackbarService:SnackbarService,
    private formErrorService:FormErrorService
  ) {}

  ngOnInit() {
    this.activatedRoute.params.subscribe(params => {
      this.paramId = params['id'];
    });
    this.getCompanyData();
    this.registerListeners();
  }

  private registerListeners() {
    this.bDataService.getBranchData.subscribe(()=>{
      this.getCompanyData(true);
    });
  }

  private getCompanyData(clearCompanyStorage?:boolean) {
    if(clearCompanyStorage) {
      localStorage.removeItem('companyData');
      this.companyData = undefined;
    }
    
    this.getStorageCompanyData();
    console.log(this.companyData);

    if (!this.companyData) {
      this.cDataService.getCompany(this.paramId).subscribe(
        response => {
          this.companyData = <GetCompany>response.body;
          this.cDataService.getCompanyLogo(this.companyData).subscribe(response=>{
            let reader = new FileReader();
                reader.addEventListener("load", () => {
                    this.companyData.logo = reader.result;
                    console.log(this.companyData.logo)
                    this.cDataService.storeCompanyData(this.companyData);
                    this.checkForCompanyOwnership();
                    this.getBranchData();
                }, false);

                if (response.body) {
                    reader.readAsDataURL(response.body);
                }
          },error=>{
            console.log("po eewejsciem");
            this.companyData.logo = this.cDataService.defaultCProfileUrl;
            this.cDataService.storeCompanyData(<GetCompany>response.body);
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
      this.userREST.companiesIDs.forEach(companyId => {
        if (this.companyData && this.companyData.companyId === companyId) {
          this.owner.next(true);
        }
      });
    }
  }

  private getBranchData() {
    let subject = new Subject<any>();
    subject.subscribe(()=>{
      this.isLoaded.next(true);
    });

    if (this.companyData) {
      this.branchData = [];
      this.companyData.branchesIDs.forEach((branchId, index) => {       
          this.bDataService.getBranch(branchId).subscribe(
            response => {
              let branchData: Branch = <Branch>response.body;
              this.bDataService.getBranchLogo(branchData).subscribe(response=>{
                let reader = new FileReader();
                reader.addEventListener("load", () => {
                    branchData.logo = reader.result;
                    branchData.branchId = branchId;
                    this.branchData.push(branchData);
                    subject.next(true);
                }, false);

                if (response.body) {
                    reader.readAsDataURL(response.body);
                }

              },error=>{
                branchData.logo = this.bDataService.defaultLogoUrl;
                branchData.branchId = branchId;
                this.branchData.push(branchData);
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

  public goBack() {
    this.canShowBranches.next(false);
    this.canShowEditForm.next(false);
    this.canShowAddBranchForm.next(false);
    this.canShowCompany.next(true);
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
      logoURL:this.companyData.logoURL?this.companyData.logoURL:undefined,
    };
    //this.canShowEditForm.next(true);
    //this.canShowCompany.next(false);
    this.router.navigate(['companyEdit'],{relativeTo:this.activatedRoute,queryParams:this.editData});
  }
}
