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
import { BehaviorSubject } from 'rxjs';
import { BranchService } from 'src/app/services/branch.service';
import { EditRequestData } from 'src/app/user/company/company.component';
import { UserREST } from 'src/app/classes/User';

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
  private successMessageText = 'Akcja została zakończona pomyślnie';
  private errorMessageText = 'Akcja niepowiodła się';
  public successMessage: string = '';
  public errorMessage: string = '';
  public canShowBranches = new BehaviorSubject(false);
  public canShowEditForm = new BehaviorSubject(false);
  public canShowNewsletters = new BehaviorSubject(false);
  public canShowAddBranchForm = new BehaviorSubject(false);
  public canShowCompany = new BehaviorSubject(true);

  constructor(
    private activatedRoute: ActivatedRoute,
    private cDataService: CompanyService,
    private router: Router,
    private bDataService: BranchService
  ) {}

  ngOnInit() {
    this.activatedRoute.params.subscribe(params => {
      this.paramId = params['id'];
    });
    this.getCompanyData();
  }
  private getCompanyData() {
    this.getStorageCompanyData();

    if (!this.companyData) {
      this.cDataService.getCompany(this.paramId).subscribe(
        response => {
          this.companyData = <GetCompany>response.body;
          this.cDataService.storeCompanyData(<GetCompany>response.body);
          this.checkForCompanyOwnership();
        },
        error => {
          this.checkForCompanyOwnership();
          this.showRequestMessage('error');
        }
      );
    }
  }

  private getStorageCompanyData() {
    if (storage_Avaliable('localStorage')) {
      let companyData: GetCompany[] = JSON.parse(
        localStorage.getItem('companyData')
      );
      companyData.forEach(company => {
        if (this.paramId == company.companyId) {
          this.companyData = company;
          this.checkForCompanyOwnership();
        }
      });
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
    if (this.companyData) {
      this.branchData = [];
      this.companyData.branchesIDs.forEach((branchId, index) => {
        this.bDataService.getBranch(branchId).subscribe(
          response => {
            let branchData: Branch = <Branch>response.body;
            branchData.branchId = branchId;
            this.branchData.push(branchData);
            if (index == this.companyData.branchesIDs.length) {
              this.isLoaded.next(true);
            }
          },
          error => {
            console.log(error);
            this.showRequestMessage('error');
          }
        );
      });
    }
  }

  public goBack() {
    this.canShowBranches.next(false);
    this.canShowEditForm.next(false);
    this.canShowAddBranchForm.next(false);
    this.canShowNewsletters.next(false);
    this.canShowCompany.next(true);
  }

  public showBranches() {
    this.getBranchData();
    this.canShowCompany.next(false);
    this.canShowBranches.next(true);
  }

  public showAddBranchform() {
    this.editData = {
      companyId: null,
      workId: null,
      addWork: true
    };
    this.canShowBranches.next(false);
    this.canShowAddBranchForm.next(true);
  }
  public showNewsletters() {
    this.canShowCompany.next(false);
    this.canShowNewsletters.next(true);
  }

  public showEditForm() {
    this.editData = {
      companyId: this.companyData.companyId,
      workId: null,
      addWork: false
    };
    this.canShowEditForm.next(true);
    this.canShowCompany.next(false);
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
}
