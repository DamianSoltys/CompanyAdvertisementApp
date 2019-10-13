import { Component, OnInit } from '@angular/core';
import { Branch, GetCompany } from 'src/app/classes/Company';
import { BranchService } from 'src/app/services/branch.service';
import { Router, ActivatedRoute } from '@angular/router';
import { storage_Avaliable } from 'src/app/classes/storage_checker';
import { BehaviorSubject } from 'rxjs';
import { Position,Marker } from 'src/app/user/company/company.component';
import { UserREST } from 'src/app/classes/User';

@Component({
  selector: 'app-branch-profile',
  templateUrl: './branch-profile.component.html',
  styleUrls: ['./branch-profile.component.scss']
})
export class BranchProfileComponent implements OnInit {
  public actualPosition: Position = {
    latitude: 51.246452,
    longitude: 22.568445
  };
  public mapMarker:Marker;

  private branchData:Branch;
  private branchId:number;
  private companyId:number;
  public owner = new BehaviorSubject(false);
  private successMessageText = 'Akcja została zakończona pomyślnie';
  private errorMessageText = 'Akcja niepowiodła się';
  public successMessage: string = '';
  public errorMessage: string = '';

  constructor(private bDataService:BranchService,private activatedRoute:ActivatedRoute,private router:Router) { }

  ngOnInit() {
    this.activatedRoute.params.subscribe(params => {
      this.branchId = params['idBranch'];
      this.companyId = params['idCompany']
    });
    this.getBranchData();
  }

  public goBack(isCompany:boolean) {
    if(isCompany) {
      this.router.navigate(['/companyProfile',this.companyId]);
    } else {
      this.router.navigate(['/search']);
    }
  }

  private checkBranchOwnership() {
    if(storage_Avaliable('localStorage')) {
      let companyList:GetCompany[] = JSON.parse(localStorage.getItem('companyData'));
      let userREST:UserREST = JSON.parse(localStorage.getItem('userREST'));
      if(companyList && userREST) {      
          userREST.companiesIDs.forEach(companyId=>{
            if(companyId == this.companyId) {
              this.owner.next(true);
            }
          });
      }
    }
  }

  public showEditForm() {
    console.log('edit');
  }
  public showComments() {
    console.log('comment');
  }

  private getBranchData() {
   this.getStorageBranchData();
   if(!this.branchData) {
     this.bDataService.getBranch(this.branchId).subscribe(response=>{
      this.branchData = <Branch>response.body;   
     },error=>{
      console.log(error);
     });
   }
   this.checkBranchOwnership(); 
   this.mapMarker = {
    latitude:Number(this.branchData.geoX),
    longitude:Number(this.branchData.geoY),
    label:'Zakład',        
  }
  }

  private getStorageBranchData() {
    if(storage_Avaliable('localStorage')) {
      let branchData:Branch[] = JSON.parse(localStorage.getItem('branchData'));
      branchData.forEach(branch=>{
        if(this.branchId == branch.branchId) {
          this.branchData = branch;
        }
      });
    }
  }

}
