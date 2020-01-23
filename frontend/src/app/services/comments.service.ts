import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Subject } from 'rxjs';
import { SearchResponse, CommentResponse } from '@interfaces/Section';
import { OpinionListData } from '@commonComponents/comments/comments.component';

export interface OpinionData {
  comment?: CommentData[];
  rating?: RatingData[];
  numberOfPages?: number;
}

export interface CommentData {
  comment?: string,
  branchId: number,
  commentId?: number,
  userId?: number,
  isOwnBranchCommented?: boolean,
  username?: string,
}

export interface RatingData {
  rating?: number,
  branchId?: number,
  ratingId?: number,
  userId?: number,
}
@Injectable({
  providedIn: 'root'
})
export class CommentsService {

  constructor(private http: HttpClient) { }

  public postOpinion(opinionData: OpinionListData, rateId?: number): Subject<boolean> {
    let commentData: CommentData = {
      comment: opinionData.comment,
      branchId: opinionData.branchId
    }
    let ratingData: RatingData = {
      rating: opinionData.rate,
      branchId: opinionData.branchId
    }
    let subject = new Subject<boolean>();

    this.http.post(`http://localhost:8090/api/comment`, commentData).subscribe(response => {
      if (opinionData.rate) {

        if (rateId) {
          this.http.patch(`http://localhost:8090/api/rating/${rateId}`, { rating: opinionData.rate }).subscribe(response => {
            subject.next(true);
          }, error => {
            subject.next(false);
          });
        } else {
          this.http.post(`http://localhost:8090/api/rating`, ratingData).subscribe(response => {
            subject.next(true);
          }, error => {
            subject.next(false);
          });
        }
      } else {
        subject.next(true);
      }
    }, error => {
      subject.next(false);
    });

    return subject;
  }

  public getOpinion(opinionData: OpinionListData, pageNumber?: number): Subject<OpinionData> {
    let subject = new Subject<OpinionData>();

    this.getComment(opinionData, pageNumber).subscribe(response => {
      let httpRatingParams = new HttpParams().set('branchId', opinionData.branchId.toString());
      this.getRating(httpRatingParams, response).subscribe(response => {
        subject.next(response);
      });
    });

    return subject;
  }

  public getRating(httpRatingParams: HttpParams, data?: OpinionData, ) {
    let subject = new Subject<OpinionData>();

    if (!data) {
      data = {};
    }
    this.http.get(`http://localhost:8090/api/rating`, { observe: 'response', params: httpRatingParams }).subscribe(response => {
      let responseData = <CommentResponse>response.body;
      data.rating = responseData.content;
      subject.next(data);
    }, error => {
      subject.next(null);
    });

    return subject;
  }

  public getComment(opinionData: OpinionListData, pageNumber?: number) {
    let data: OpinionData = {};
    let subject = new Subject<OpinionData>();
    let httpCommentParams: HttpParams;

    if (!pageNumber) {
      httpCommentParams = new HttpParams().set('branchId', opinionData.branchId.toString()).set('size', '3');
    } else {
      httpCommentParams = new HttpParams().set('branchId', opinionData.branchId.toString()).set('size', '3').set('page', pageNumber.toString());
    }
    this.http.get(`http://localhost:8090/api/comment`, { observe: 'response', params: httpCommentParams }).subscribe(response => {
      let responseData = <CommentResponse>response.body;
      data.comment = responseData.content;
      data.numberOfPages = responseData.totalPages;
      subject.next(data);
    }, error => {
      subject.next(null);
    });

    return subject;
  }
}
