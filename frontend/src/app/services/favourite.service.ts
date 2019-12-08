import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { PersonalData } from '../classes/User';
import { Observable, Subject } from 'rxjs';

export interface FavouritePost {
    userId:number,
    branchId:number
}

export interface FavouriteResponse {
  branchId:number,
  favouriteBranchId:string,
  userId:number,
}

@Injectable({
  providedIn: 'root'
})
export class FavouriteService {
  constructor(private http: HttpClient) {}

  public getFavouriteBranches(userId:number) {
      let subject = new Subject<any>();
        this.http.get(`http://localhost:8090/api/favourite/user/${userId}`,{observe:'response'}).subscribe(response=>{
            let favData:FavouriteResponse[] = response.body as FavouriteResponse[];
            subject.next(favData);
        },error=>{
            subject.next(false);
        });
      return subject;
  }

  public setFavouriteBranch(favouriteIds:FavouritePost) {
    let subject = new Subject<any>();
      this.http.post(`http://localhost:8090/api/favourite`,favouriteIds).subscribe(response=>{
          console.log(response);
          subject.next(true);
      },error=>{
          subject.next(false);
      });
    return subject;
  }   

  public deleteFavouriteBranch(favouriteId:string) {
    let subject = new Subject<any>();
      this.http.delete(`http://localhost:8090/api/favourite/${favouriteId}`,{observe:'response'}).subscribe(response=>{
          console.log(response);
          subject.next(true);
      },error=>{
          subject.next(false);
      });
    return subject;
  } 
  
  public checkifBranchFav(favBranches:any,favouriteData:FavouritePost) {

  }
}