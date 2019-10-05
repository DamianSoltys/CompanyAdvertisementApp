import { Component, OnInit } from '@angular/core';

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
  constructor() {
    this.editData = {
      companyId: null,
      workId: 5,
    };
    
   }

  ngOnInit() {
  }

}
