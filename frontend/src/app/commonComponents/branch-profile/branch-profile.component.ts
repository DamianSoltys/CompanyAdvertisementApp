import { Component, OnInit } from '@angular/core';
import { Branch, GetCompany } from 'src/app/classes/Company';
import { BranchService } from 'src/app/services/branch.service';
import { Router, ActivatedRoute } from '@angular/router';
import { storage_Avaliable } from 'src/app/classes/storage_checker';
import { BehaviorSubject } from 'rxjs';
import { Position, Marker, EditRequestData } from 'src/app/user/company/company.component';
import { UserREST } from 'src/app/classes/User';
import { SnackbarService } from 'src/app/services/snackbar.service';

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
  public mapMarker: Marker;

  private branchData: Branch;
  private branchId: number;
  private companyId: number;
  public owner = new BehaviorSubject(false);
  public editData:EditRequestData;

  constructor(
    private bDataService: BranchService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private snackbarService:SnackbarService,
  ) {}

  ngOnInit() {
    this.activatedRoute.params.subscribe(params => {
      this.branchId = params['idBranch'];
      this.companyId = params['idCompany'];
    });

    this.getBranchData();
    this.registerBranchListener();
    console.log(this.branchData)
  }

  public registerBranchListener() {
    this.bDataService.getBranchData.subscribe(()=>{
      this.getBranchData(true);
    });
  }
  public goBack(isCompany: boolean) {
    if (isCompany) {
      this.router.navigate(['/companyProfile', this.companyId]);
    } else {
      this.router.navigate(['/search']);
    }
  }

  private checkBranchOwnership() {
    if (storage_Avaliable('localStorage')) {
      let companyList: GetCompany[] = JSON.parse(
        localStorage.getItem('companyData')
      );
      let userREST: UserREST = JSON.parse(localStorage.getItem('userREST'));
      if (companyList && userREST) {
        if(userREST.companiesIDs) {
          userREST.companiesIDs.forEach(companyId => {
            if (companyId == this.companyId) {
              this.owner.next(true);
            }
          });
        }
      }
    }
  }

  public showEditForm() {
    this.editData = {
      companyId: null,
      workId: this.branchData.branchId,
      addWork: null,
      backId:this.companyId,
      logoKey:this.branchData.logoKey,
      logoURL:this.branchData.logoURL,
    };
    this.router.navigate(['edit'],{relativeTo:this.activatedRoute,queryParams:this.editData});
  }

  private getBranchData(clearDataStorage?:boolean) {
    if(clearDataStorage) {
      this.bDataService.deleteStorageData();
    }
    this.getStorageBranchData();
    if (!this.branchData) {
      this.bDataService.getBranch(this.branchId).subscribe(
        response => {
          this.branchData = <Branch>response.body;
          this.bDataService.getBranchLogo(this.branchData).subscribe(response=>{
            if(response.status !=204) {
              let reader = new FileReader();
            reader.addEventListener("load", () => {
                this.branchData.logo = reader.result;
                this.checkBranchOwnership();
                this.mapMarker = {
                  latitude: Number(this.branchData.geoX),
                  longitude: Number(this.branchData.geoY),
                  label: 'Zakład'
                };
            }, false);

            if (response.body) {
                reader.readAsDataURL(response.body);
            }
            } else {
              this.branchData.logo = this.bDataService.defaultLogoUrl;
              this.checkBranchOwnership();
              this.mapMarker = {
                latitude: Number(this.branchData.geoX),
                longitude: Number(this.branchData.geoY),
                label: 'Zakład'
              };
            }
          
          },error=>{
            this.branchData.logo = this.bDataService.defaultLogoUrl;
            this.checkBranchOwnership();
            this.mapMarker = {
              latitude: Number(this.branchData.geoX),
              longitude: Number(this.branchData.geoY),
              label: 'Zakład'
            };
          });
        },
        error => {
          console.log(error);
        }
      );
    } else {
      this.checkBranchOwnership();
      this.mapMarker = {
        latitude: Number(this.branchData.geoX),
        longitude: Number(this.branchData.geoY),
        label: 'Zakład'
      };
    }
    
  }

  private getStorageBranchData() {
    if (storage_Avaliable('localStorage')) {
      let branchData: Branch[] = JSON.parse(localStorage.getItem('branchData'));
      if(branchData) {
        branchData.forEach(branch => {
          if (this.branchId == branch.branchId) {
            this.branchData = branch;
          }
        });
      }
    }
  }
}
