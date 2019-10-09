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
  @Input() branchData:Branch;
  @Input() showWorks: boolean;
  public owner = new BehaviorSubject(false);
  public works: Branch[];
  constructor(
    private cDataService: CompanyService,
    private uDataService: UserService,
    private bDataService: BranchService,
  ) {}

  ngOnInit() {
    if (storage_Avaliable('localStorage')) {
      let userREST: UserREST = JSON.parse(localStorage.getItem('userREST'));
      if (userREST) {
        if(!this.showWorks) {
          userREST.companiesIDs.forEach(value => {
            if (value === this.companyData.companyId) this.owner.next(true);
          });
        } else {
          this.companyData.branchesIDs.forEach((branchId)=>{
            if(branchId === this.branchData.branchId) this.owner.next(true);
          });
        }
      }
    } 
  }

  public deleteCompany() {
    this.cDataService.deleteCompany(this.companyData.companyId).subscribe(
      response => {
        this.uDataService.updateUser();
        setTimeout(() => {
          location.reload();
        }, 200);
      },
      error => {
        console.log(error);
      }
    );
  }

  public deleteBranch() {
    console.log('delete');
  }
  ngOnDestroy() {
    if (!this.showWorks) {
      this.cDataService.CompanyData[this.companyData.companyId] = this.companyData;
    }
  }
}
