import { Component, OnInit } from '@angular/core';
import { Branch, GetCompany } from 'src/app/classes/Company';
import { BranchService } from 'src/app/services/branch.service';
import { Router, ActivatedRoute } from '@angular/router';
import { storage_Avaliable } from 'src/app/classes/storage_checker';
import { BehaviorSubject } from 'rxjs';
import { Position, Marker, EditRequestData } from 'src/app/mainComponents/user/company/company.component';
import { UserREST } from 'src/app/classes/User';
import { SnackbarService, SnackbarType } from 'src/app/services/snackbar.service';
import { RecommendationService } from 'src/app/services/recommendation.service';
import { CompanyService } from 'src/app/services/company.service';
import { FavouriteService, FavouritePost, FavouriteResponse } from 'src/app/services/favourite.service';

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
  public userData:UserREST;
  public favouriteData:FavouriteResponse[];
  public isFavourite:boolean = false;
  private favUuid:string;

  constructor(
    private bDataService: BranchService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private snackbarService:SnackbarService,
    private rDataService:RecommendationService,
    private cDataService:CompanyService,
    private fDataService:FavouriteService
  ) {}

  ngOnInit() {
    this.activatedRoute.params.subscribe(params => {
      this.branchId = params['idBranch'];
      this.companyId = params['idCompany'];
    });
    this.getUserData();
    this.getBranchData();
    this.registerBranchListener();
    this.getFavBranches();
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
      if (companyList && this.userData) {
        if(this.userData.companiesIDs) {
          this.userData.companiesIDs.forEach(companyId => {
            if (companyId == this.companyId) {
              this.owner.next(true);
            }
          });
        }
      }
    }
  }

  public showEditForm() {
    let editData = this.branchData;
    editData.logo = null;
    this.editData = {
      companyId: null,
      workId: this.branchData.branchId,
      addWork: null,
      backId:this.companyId,
      logoKey:this.branchData.logoKey,
      putLogoUrl:this.branchData.putLogoURL,
      getLogoUrl:this.branchData.getLogoURL,
      branchData:JSON.stringify(editData)
    };
    this.router.navigate(['edit'],{relativeTo:this.activatedRoute,queryParams:this.editData});
  }

  public setFavouriteBranch() {
    let favouriteData:FavouritePost = {
      userId:this.userData.userID,
      branchId:this.branchId
    }
    this.fDataService.setFavouriteBranch(favouriteData).subscribe(response=>{
      if(response) {
        this.getFavBranches();
        this.snackbarService.open({
          message:'Dodanie do ulubionych powiodło się!',
          snackbarType:SnackbarType.success
        });
      } else {
        this.snackbarService.open({
          message:'Dodanie do ulubionych się nie powiodło!',
          snackbarType:SnackbarType.error
        });
      }
    })
  }

  private getFavBranches() {
    if(this.userData) {
      this.fDataService.getFavouriteBranches(this.userData.userID).subscribe(response=>{
        if(response) {
          this.favouriteData = response;
          this.isFavourite = false;
          this.favouriteData.forEach(fav=>{
            if(this.branchId == fav.branchId) {
              this.isFavourite = true;
              this.favUuid = fav.favouriteBranchId;
            }
          });
        } else {
          this.isFavourite = false;
        }
      });
    }
  }

  public deleteFavouriteBranch() {
    if(this.favUuid) {
      this.fDataService.deleteFavouriteBranch(this.favUuid).subscribe(response=>{
        if(response){
          this.getFavBranches();
          this.snackbarService.open({
            message:'Usunięcie z ulubionych się powiodło!',
            snackbarType:SnackbarType.success
          });
        } else {
          this.snackbarService.open({
            message:'Usunięcie z ulubionych się nie powiodło!',
            snackbarType:SnackbarType.error
          });
        }
      });
    }
  }

  private getUserData() {
    if(storage_Avaliable('localStorage')) {
      let userREST: UserREST = JSON.parse(localStorage.getItem('userREST')); 
      if(userREST) {
        this.userData = userREST;
      } else {
        console.log('Użytkownik nie jest zalogowany');
      }   
    } else {
      console.log('Storage nie jest dostępny');
    }
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
          this.branchData.branchId = this.branchId;
          if(this.branchData) {
            this.rDataService.pushRecommendationData(this.branchData.category,this.branchData.branchId);
          }
          this.bDataService.getBranchLogo(this.branchData).subscribe(response=>{
            if(response.status !=204) {
              let reader = new FileReader();
            reader.addEventListener("load", () => {
                this.branchData.logo = reader.result;
                this.checkBranchOwnership();
                this.bDataService.storeBranchData(this.branchData);
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
              this.bDataService.storeBranchData(this.branchData);
              this.mapMarker = {
                latitude: Number(this.branchData.geoX),
                longitude: Number(this.branchData.geoY),
                label: 'Zakład'
              };
            }
          
          },error=>{
            this.branchData.logo = this.bDataService.defaultLogoUrl;
            this.checkBranchOwnership();
            this.bDataService.storeBranchData(this.branchData);
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

  private setRecommendationData() {
    
  }

  private getStorageBranchData() {
    if (storage_Avaliable('localStorage')) {
      let branchData: Branch[] = JSON.parse(localStorage.getItem('branchData'));
      if(branchData) {
        branchData.forEach(branch => {
          if (this.branchId == branch.branchId) {
            this.branchData = branch;

            if(this.branchData) {
              this.rDataService.pushRecommendationData(this.branchData.category,this.branchData.branchId);
            }
          }
        });
      }
    }
  }
}
