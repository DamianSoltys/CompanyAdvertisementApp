import { Component, OnInit } from '@angular/core';
import { FavouriteResponse, FavouriteService } from 'src/app/services/favourite.service';
import { Branch } from 'src/app/classes/Company';
import { CommentResponse } from 'src/app/classes/Section';
import { storage_Avaliable } from 'src/app/classes/storage_checker';
import { UserREST } from 'src/app/classes/User';
import { BranchService } from 'src/app/services/branch.service';
import { CommentsService, OpinionData, RatingData } from 'src/app/services/comments.service';
import { HttpParams } from '@angular/common/http';
import { SnackbarService, SnackbarType } from 'src/app/services/snackbar.service';
export interface FavBranchData {
  favData?:FavouriteResponse,
  branchData?:Branch,
  rating?:RatingData[],
  avgRate?:number,
}

@Component({
  selector: 'app-favourite-branches',
  templateUrl: './favourite-branches.component.html',
  styleUrls: ['./favourite-branches.component.scss']
})
export class FavouriteBranchesComponent implements OnInit {
  public favBranchesData:FavouriteResponse[];
  public branchData:FavBranchData[] =[];
  public userData:UserREST;
  public loaded:boolean = false;
  constructor(private fDataService:FavouriteService,private bDataService:BranchService,private cDataService:CommentsService,private sDataService:SnackbarService) { }

  ngOnInit() {
    this.getUserData();
    this.getFavBranches();
  }

  public getUserData() {
    if(storage_Avaliable('localStorage')) {
      this.userData = JSON.parse(localStorage.getItem('userREST'));
    }
  }

  public getFavBranches() {
    if(this.userData) {
      this.branchData = [];
      this.fDataService.getFavouriteBranches(this.userData.userID).subscribe(response=>{
        this.favBranchesData = response;
        if(response) {
          if(!response.length) {
            this.loaded = true;  
          }
          this.favBranchesData.forEach(fav=>{
            let favResponse:FavBranchData = {};
            favResponse.favData = fav;
            this.bDataService.getBranch(fav.branchId).subscribe(response=>{
              if(response) {
                favResponse.branchData = response.body as Branch;
                let httpParams = new HttpParams().set('branchId',favResponse.favData.branchId.toString());
                this.cDataService.getRating(httpParams).subscribe(response=>{
                  if(response) {
                    favResponse.rating = response.rating;
                    favResponse.avgRate = this.calculateAvgRate(favResponse.rating);
                    this.bDataService.getBranchLogo(favResponse.branchData).subscribe(response=>{
                      if(response.status !=204) {
                        let reader = new FileReader();
                      reader.addEventListener("load", () => {
                          favResponse.branchData.logo = reader.result;
                          this.branchData.push(favResponse);
                          this.loaded = true;                                 
                      }, false);
          
                      if (response.body) {
                          reader.readAsDataURL(response.body);
                      }
                      } else {
                        favResponse.branchData.logo = this.bDataService.defaultLogoUrl;
                        this.branchData.push(favResponse);
                        this.loaded = true;                 
                      }
                    });
                  }
                });               
              }
            });
          });
        } else {
          this.loaded = true;  
        }
      });
    }
  }

  private calculateAvgRate(rateData:RatingData[]):number {
    let avgRate:number = 0;
    rateData.forEach(data=>{
      avgRate+=data.rating;
    });
    return avgRate/=rateData.length;
  }

  public deleteFavBranch(favId:string) {
    this.fDataService.deleteFavouriteBranch(favId).subscribe(response=>{
      if(response) {
        this.sDataService.open({
          message:'Pomyślnie usunięto zakład z ulubionych!',
          snackbarType:SnackbarType.success
        });
        this.getFavBranches();
      } else {
        this.sDataService.open({
          message:'Usunięcie zakładu z ulubionych nie powiodło się!',
          snackbarType:SnackbarType.error
        });
      }
    })
  }

}
