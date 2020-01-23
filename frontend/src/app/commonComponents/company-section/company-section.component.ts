import { Component, OnInit, Input, OnDestroy } from '@angular/core';
import { Company, GetCompany, Branch } from '@interfaces/Company';
import { storage_Avaliable } from '@interfaces/storage_checker';
import { UserREST } from '@interfaces/User';
import { BehaviorSubject } from 'rxjs';
import { CompanyService } from '@services/company.service';
import { UserService } from '@services/user.service';
import { BranchService } from '@services/branch.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-company-section',
  templateUrl: './company-section.component.html',
  styleUrls: ['./company-section.component.scss']
})
export class CompanySectionComponent implements OnInit, OnDestroy {
  @Input() companyData: GetCompany;
  @Input() branchData: Branch;
  @Input() showWorks: boolean;
  public owner = new BehaviorSubject(false);
  public works: Branch[];

  constructor(
    private cDataService: CompanyService,
    private uDataService: UserService,
    private bDataService: BranchService
  ) { }

  ngOnInit() {
    if (storage_Avaliable('localStorage')) {
      let userREST: UserREST = JSON.parse(localStorage.getItem('userREST'));

      if (userREST) {
        if (this.companyData) {
          if (userREST.companiesIDs) {
            userREST.companiesIDs.forEach(value => {
              if (value === this.companyData.companyId) this.owner.next(true);
            });
          }
        }
      }
    }
  }

  public showBranches() {
    if (this.showWorks && this.companyData && this.branchData) {
      return true;
    } else {
      return false;
    }
  }

  public deleteCompany() {
    this.cDataService.deleteCompany(this.companyData.companyId).subscribe(
      response => {
        this.uDataService.updateUser().subscribe(() => {
          this.cDataService.removeCompanyFromLocalStorage(this.companyData.companyId);
          this.cDataService.getCompanyData.next(false);
        });
      },
      error => {
        console.log(error);
      }
    );
  }

  public deleteBranch() {
    this.bDataService.deleteBranch(this.branchData.branchId).subscribe(
      response => {
        this.uDataService.updateUser().subscribe(() => {
          this.bDataService.getBranchData.next(true);
        });
      },
      error => {
        console.log(error);
      }
    );
  }

  ngOnDestroy() {
    if (this.companyData && !this.showWorks) {
      //this.cDataService.storeCompanyData(this.companyData);
    }
    if (this.branchData && this.showWorks) {
      //this.bDataService.storeBranchData(this.branchData);
    }
  }
}
