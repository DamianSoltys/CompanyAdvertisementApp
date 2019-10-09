import { Component, OnInit, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { Router, ParamMap, ActivatedRoute } from '@angular/router';
import { GetCompany, Branch } from 'src/app/classes/Company';
import { CompanyService } from 'src/app/services/company.service';
import { storage_Avaliable } from 'src/app/classes/storage_checker';
import { BehaviorSubject } from 'rxjs';
import { BranchService } from 'src/app/services/branch.service';
import { EditRequestData } from 'src/app/user/company/company.component';

@Component({
  selector: 'app-company-profile',
  templateUrl: './company-profile.component.html',
  styleUrls: ['./company-profile.component.scss']
})
export class CompanyProfileComponent implements OnInit {
  public editData:EditRequestData;
  public paramId: number;
  public owner:boolean;
  public companyData: GetCompany;
  public branchData:Branch[];
  private successMessageText = 'Akcja została zakończona pomyślnie';
  private errorMessageText = 'Akcja niepowiodła się';
  public successMessage: string = '';
  public errorMessage: string = '';
  public canShowComments = new BehaviorSubject(false);
  public canShowBranches = new BehaviorSubject(false);
  public canShowEditForm = new BehaviorSubject(false);
  public canShowNewsletters= new BehaviorSubject(false);
  public canShowAddBranchForm = new BehaviorSubject(false);
  public canShowCompany = new BehaviorSubject(true);

  constructor(private activatedRoute: ActivatedRoute, private cDataService: CompanyService,private router:Router,private bDataService:BranchService) {}

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

  private getBranchData() {
    if(this.companyData) {
      this.branchData = [];
      this.companyData.branchesIDs.forEach(branchId=>{
        this.bDataService.getBranch(branchId).subscribe(response=>{
          let branchData:Branch = <Branch>response.body;
          branchData.branchId = branchId;
          this.branchData.push(branchData);
        },error=>{
          console.log(error);
          this.showRequestMessage('error');
        })
      });
    }
  }

  public goBack() {
    this.canShowComments.next(false);
    this.canShowBranches.next(false);
    this.canShowEditForm.next(false);
    this.canShowAddBranchForm.next(false);
    this.canShowNewsletters.next(false);
    this.canShowCompany.next(true);
  }

  public showComments() {
    this.canShowComments.next(true);
    this.canShowCompany.next(false);
  }

  public showBranches() {
    this.getBranchData();
    this.canShowCompany.next(false);
    this.canShowBranches.next(true);
  }

  public showAddBranchform() {
    this.editData ={
      companyId:null,
      workId:null,
      addWork:true,
    }   
    this.canShowBranches.next(false);
    this.canShowAddBranchForm.next(true);
  }
  public showNewsletters() {
    this.canShowCompany.next(false);
    this.canShowNewsletters.next(true);
  }

  public showEditForm() {    
      this.editData ={
        companyId:this.companyData.companyId,
        workId:null,
        addWork:false,
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
