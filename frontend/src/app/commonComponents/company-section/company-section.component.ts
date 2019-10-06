import { Component, OnInit, Input, OnDestroy } from '@angular/core';
import { Company,GetCompany } from 'src/app/classes/Company';
import { storage_Avaliable } from 'src/app/classes/storage_checker';
import { UserREST } from 'src/app/classes/User';
import { BehaviorSubject } from 'rxjs';
import { CompanyService } from 'src/app/services/company.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-company-section',
  templateUrl: './company-section.component.html',
  styleUrls: ['./company-section.component.scss']
})
export class CompanySectionComponent implements OnInit,OnDestroy {
  @Input() companyData:GetCompany;
  public owner = new BehaviorSubject(false);
  constructor(private cDataService:CompanyService,private uDataService:UserService) { }

  ngOnInit() {
    console.log(this.companyData);
    if(storage_Avaliable('localStorage')) {
      let userREST:UserREST = JSON.parse(localStorage.getItem('userREST'));
      userREST.companiesIDs.forEach(value=>{
        if(value === this.companyData.companyId) this.owner.next(true);
      });
    }
  }

  public deleteCompany() {
    this.cDataService.deleteCompany(this.companyData.companyId).subscribe(response=>{
      this.uDataService.updateUser();
      setTimeout(()=>{
        location.reload();
      },200);
    },error=>{
      console.log(error);
    });
    
  }
  ngOnDestroy() {
    this.cDataService.CompanyData[this.companyData.companyId] = this.companyData;
  }


}
