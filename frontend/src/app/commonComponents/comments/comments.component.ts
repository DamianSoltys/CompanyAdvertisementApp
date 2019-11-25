import { Component, OnInit, ViewChild, ElementRef, HostListener } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';

import { BehaviorSubject, Subject } from 'rxjs';
import {
  trigger,
  state,
  style,
  animate,
  transition,
  query
} from '@angular/animations';
import {
  CommentsService,
  OpinionData
} from 'src/app/services/comments.service';
import { UserService } from 'src/app/services/user.service';
import { UserREST } from 'src/app/classes/User';
import { ActivatedRoute } from '@angular/router';
import { SnackbarService, SnackbarType } from 'src/app/services/snackbar.service';
import { CompanyService } from 'src/app/services/company.service';
export interface OpinionListData {
  comment?:string,
  rate?:number
  userName?:string,
  userId?:number,
  branchId?:number,
  ratingId?:number,
  isOwner?:boolean,
  commentId?:number,
}
@Component({
  selector: 'app-comments',
  templateUrl: './comments.component.html',
  styleUrls: ['./comments.component.scss'],
  animations: [
    trigger('formAnimate', [
      transition(':enter', [
        style({ opacity: 0 }),
        animate('500ms', style({ visibility: 'visible', opacity: 1 }))
      ]),
      transition(':leave', [
        style({ opacity: 1 }),
        animate('500ms', style({ visibility: 'hidden', opacity: 0 }))
      ])
    ])
  ]
})
export class CommentsComponent implements OnInit {
  @ViewChild('scrollContainer') scrollContainer:ElementRef;
  public ratingForm = this.fb.group({
    commentText: ['', [Validators.required]]
  });
  public isForm = new BehaviorSubject(false);
  public isEditForm = new BehaviorSubject(false);
  public isLoaded = new BehaviorSubject(false);
  public isOwner = new BehaviorSubject(false);
  public currentRate = this.isOwner.value?1:null;
  public numberOfPages:number;
  public actualPageLoaded:number = 0;

  public config = {
    toolbar: [['bold', 'italic', 'underline']]
  };
  public userREST: UserREST;
  public branchId: string;
  public companyId:string;
  public opinions:OpinionListData[] = [];
  public opinionData:OpinionListData;
  constructor(
    private fb: FormBuilder,
    private cDataService: CommentsService,
    private uDataService: UserService,
    private activatedRoute: ActivatedRoute,
    private sDataService:SnackbarService,
    private coDataService:CompanyService
  ) {}

  ngOnInit() {
    this.activatedRoute.parent.params.subscribe(params => {
      this.branchId = params['idBranch'];
      this.companyId = params['idCompany'];
    });
    this.getData();
    console.log(this.userREST);
  }

  public getData() {
    this.uDataService.userREST.subscribe(data => {
      this.userREST = data;
      if(this.userREST) {
        if(this.userREST.userID) {
          this.opinionData = {
            userId:this.userREST.userID,
            branchId:Number(this.branchId),
          };
        }
      } else {
        this.opinionData = {
          userId:null,
          branchId:Number(this.branchId),
        };
      }
      this.getOpinions();
    });
  }

  public showForm() {
    this.isForm.next(!this.isForm.value);
  }
  public onSubmit($event) {
    event.preventDefault();

    this.opinionData.comment = this.ratingForm.controls.commentText.value;
    if(this.currentRate) {
      this.opinionData.rate = this.currentRate;
    } else {
      this.opinionData.rate = null;
    }
    let rateId = null;
    if(this.opinions) {
      this.opinions.forEach(opinion=>{
        if(opinion.userId == this.opinionData.userId) {
          
          rateId = opinion.ratingId;
        }
      });
    }
    this.cDataService.postOpinion(this.opinionData,rateId).subscribe(response => {
      if(response) {
        this.sDataService.open({
          message:'Pomyślnie dodano opinie!',
          snackbarType:SnackbarType.success,
        });
        this.getOpinions(null,true);
        this.isForm.next(false);
      } else {
        this.sDataService.open({
          message:'Nie udało się dodać opinii!',
          snackbarType:SnackbarType.error,
        });
      }
    });
  }

  public canShowAddButton() {
    if(this.userREST) {
      return true;
    } else {
      return false;
    }
  }

  public canShowRatingForm() {
    if(!this.isOwner.value) {
      return true;
    } else {
      return false;
    }
  }
  public canShowEmptyData() {
    if(this.opinions) {
      if(this.opinions.length) {
        return false;
      } else {
        return true;
      }
    } else {
      return true;
    }
  }


  @HostListener('scroll', ['$event'])
  public onScroll(event:any) {
    //dokonczyc
    let tracker = event.target;
    let limit = tracker.scrollHeight - tracker.clientHeight;
    if (event.target.scrollTop === limit) {
      if(this.actualPageLoaded < this.numberOfPages) {
        this.getOpinions(this.actualPageLoaded+1);
        this.actualPageLoaded++;
      }
    }
  }
  
  public getOpinions(pageNumber?:number,clearOpinions?:boolean) {
    let subject = new Subject<boolean>();
    subject.subscribe(data=>{
      this.isLoaded.next(true);
    });
    if(clearOpinions) {
      this.opinions = [];
    }
    this.cDataService.getOpinion(this.opinionData,pageNumber).subscribe(data=>{
      console.log(data)
      this.numberOfPages = data.numberOfPages;
      data.comment.forEach(comment => {
        let opinion:OpinionListData = {
          comment: comment.comment,
          userName: comment.username,
          commentId: comment.commentId,
          userId: comment.userId,
          isOwner:comment.isOwnBranchCommented,
        }
        this.opinions.push(opinion);
      });

      data.rating.forEach(rate=>{
        this.opinions.map(opinion => {
          if(rate.userId === opinion.userId) {
            opinion.rate = rate.rating;
            opinion.ratingId = rate.ratingId;
          } 
        });
      });

      console.log(this.opinions);
      this.isOwner.next(this.coDataService.checkForUserPermission(Number(this.companyId)));
      subject.next(true);
    },error=>{
      subject.next(false);
    });
  }
}
