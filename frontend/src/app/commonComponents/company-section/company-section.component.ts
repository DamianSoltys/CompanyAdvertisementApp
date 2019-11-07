import { Component, OnInit, Input, OnDestroy } from '@angular/core';
import { Company, GetCompany, Branch } from 'src/app/classes/Company';
import { storage_Avaliable } from 'src/app/classes/storage_checker';
import { UserREST } from 'src/app/classes/User';
import { BehaviorSubject } from 'rxjs';
import { CompanyService } from 'src/app/services/company.service';
import { UserService } from 'src/app/services/user.service';
import { BranchService } from 'src/app/services/branch.service';
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
  ) {}

  ngOnInit() {
    if (storage_Avaliable('localStorage')) {
      let userREST: UserREST = JSON.parse(localStorage.getItem('userREST'));
      if (userREST) {
        if (this.companyData) {
          userREST.companiesIDs.forEach(value => {
            if (value === this.companyData.companyId) this.owner.next(true);
          });
        }
      }
    }
    console.log(this.branchData)
  }
  public showBranches() {
    if(this.showWorks && this.companyData && this.branchData) {
      return true;
    }else {
      return false;
    }
  }

  public deleteCompany() {
    this.cDataService.deleteCompany(this.companyData.companyId).subscribe(
      response => {
        this.uDataService.updateUser().subscribe(()=>{
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
        this.uDataService.updateUser().subscribe(()=>{
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
