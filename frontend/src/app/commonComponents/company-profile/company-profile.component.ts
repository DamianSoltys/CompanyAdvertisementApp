import { Component, OnInit } from '@angular/core';
import { Router, ParamMap, ActivatedRoute } from '@angular/router';
import { GetCompany } from 'src/app/classes/Company';
import { CompanyService } from 'src/app/services/company.service';

interface EditRequestData {
  companyId:number;
  workId:number;
}
@Component({
  selector: 'app-company-profile',
  templateUrl: './company-profile.component.html',
  styleUrls: ['./company-profile.component.scss']
})
export class CompanyProfileComponent implements OnInit {
  public editData:EditRequestData;
  public param:string;
  public companyData:GetCompany;
  constructor(private activatedRoute: ActivatedRoute,private cDataService:CompanyService) {  }

  ngOnInit() {
    this.activatedRoute.params.subscribe(params=>{
      this.param = params['id'];
    });

    this.companyData = this.cDataService.CompanyData[this.param];
    console.log(this.companyData);
  }

}
