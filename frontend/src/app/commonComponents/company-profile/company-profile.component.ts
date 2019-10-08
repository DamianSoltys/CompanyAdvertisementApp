import { Component, OnInit, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { Router, ParamMap, ActivatedRoute } from '@angular/router';
import { GetCompany } from 'src/app/classes/Company';
import { CompanyService } from 'src/app/services/company.service';
import { storage_Avaliable } from 'src/app/classes/storage_checker';
import { BehaviorSubject } from 'rxjs';

interface EditRequestData {
  companyId: number;
  workId: number;
}
@Component({
  selector: 'app-company-profile',
  templateUrl: './company-profile.component.html',
  styleUrls: ['./company-profile.component.scss']
})
export class CompanyProfileComponent implements OnInit {
  public editData: EditRequestData;
  public paramId: number;
  public owner:boolean;
  public companyData: GetCompany;
  private successMessageText = 'Akcja została zakończona pomyślnie';
  private errorMessageText = 'Akcja niepowiodła się';
  public successMessage: string = '';
  public errorMessage: string = '';
  public canShowComments = new BehaviorSubject(false);
  public canShowBranches = new BehaviorSubject(false);
  public canShowEditForm = new BehaviorSubject(false);
  public canShowCompany = new BehaviorSubject(true);

  constructor(private activatedRoute: ActivatedRoute, private cDataService: CompanyService,private router:Router) {}

  ngOnInit() {
    this.activatedRoute.params.subscribe(params => {
      this.paramId = params['id'];
    });
    this.activatedRoute.data.subscribe(data=>{
      this.owner = data['owner'];
    });
    this.getCompanyData();
  }
  private getCompanyData() {
    if (this.cDataService.CompanyData[this.paramId]) {
      this.companyData = this.cDataService.CompanyData[this.paramId];
    } else {
      this.cDataService.getCompany(this.paramId).subscribe(
        response => {
          this.companyData = <GetCompany>response.body;
        },
        error => {
          console.log(error);
          this.showRequestMessage('error');
        }
      );
    }
  }
  public goBack() {
    this.canShowComments.next(false);
    this.canShowBranches.next(false);
    this.canShowEditForm.next(false);
    this.canShowCompany.next(true);
  }

  public showComments() {
    this.canShowComments.next(true);
    this.canShowCompany.next(false);
  }

  public showBranches() {
    this.canShowBranches.next(true);
    this.canShowCompany.next(false);
  }

  public showEditForm() {    
      this.editData ={
        companyId:this.companyData.companyId,
        workId:null,
      }          
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