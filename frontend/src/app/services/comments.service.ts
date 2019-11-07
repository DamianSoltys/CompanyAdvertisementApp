import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Subject } from 'rxjs';
import { SearchResponse } from '../classes/Section';

export interface OpinionData {
  comment?:any;
  rating?:any;
  branchId?:number;
  userId?:number;
}

interface CommentData {
  comment?:string;
  branchId:number;
}

interface RatingData {
  rating?:number;
  branchId:number;
}
@Injectable({
  providedIn: 'root'
})
export class CommentsService {

  constructor(private http:HttpClient) { }

   public postOpinion(opinionData:OpinionData):Subject<boolean> {
    let commentData:CommentData = <CommentData>opinionData;
    let ratingData:RatingData = <RatingData>opinionData;
    let subject = new Subject<boolean>();
    console.log(commentData)
     this.http.post(`http://localhost:8090/api/comment`,commentData).subscribe(response=>{
      this.http.post(`http://localhost:8090/api/rating`,ratingData).subscribe(response=>{
        subject.next(true);
      },error=>{
        console.log(error);
        subject.next(false);
      });
     },error=>{
       console.log(error);
       subject.next(false);
     });

     return subject;
  }

  public getOpinion(opinionData:OpinionData):Subject<OpinionData> {
    let data:OpinionData ={};
    let subject = new Subject<OpinionData>();
    console.log(opinionData)
    let httpParams= new HttpParams().set('branchId',opinionData.branchId.toString());
    console.log(httpParams)
    this.http.get(`http://localhost:8090/api/comment`,{observe:'response',params:httpParams}).subscribe(response=>{
      console.log(response);
       let responseData = <SearchResponse>response.body;
       data.comment = responseData.content;
      this.http.get(`http://localhost:8090/api/rating`,{observe:'response',params:httpParams}).subscribe(response=>{
        let responseData = <SearchResponse>response.body;
        data.rating = responseData.content;
        subject.next(data);
      },error=>{
        console.log(error);
        subject.next(null);
      });
     },error=>{
       console.log(error);
       subject.next(null);
     });
    return subject;
  }
}
