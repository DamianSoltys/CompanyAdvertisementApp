import { Component, OnInit } from '@angular/core';
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
  public ratingForm = this.fb.group({
    commentText: ['', [Validators.required]]
  });
  public isForm = new BehaviorSubject(false);
  public isEditForm = new BehaviorSubject(false);
  public isLoaded = new BehaviorSubject(false);
  public isOwner = new BehaviorSubject(false);
  public currentRate = 1;
  public config = {
    toolbar: [['bold', 'italic', 'underline']]
  };
  public userREST: UserREST;
  public branchId: string;
  public companyId:string;
  public opinions:any[];
  public opinionData:OpinionData;
  public testData = [
    {comment:'dupatreretaeretaaetatae',user:'user',rating:5},
    {comment:'sasasapdasdasdetatae',user:'Lamus',rating:2},
    {comment:'dupatreretaeretaaetatae',user:'user',rating:5},
    {comment:'sasasapdasdasdetatae',user:'Lamus',rating:2},
    {comment:'dupatreretaeretaaetatae',user:'user',rating:5},
    {comment:'sasasapdasdasdetatae',user:'Lamus',rating:2},
    {comment:'dupatreretaeretaaetatae',user:'user',rating:5},
    {comment:'sasasapdasdasdetatae',user:'Lamus',rating:2},
    {comment:'dupatreretaeretaaetatae',user:'user',rating:5},
    {comment:'sasasapdasdasdetatae',user:'Lamus',rating:2},
    {comment:'dupatreretaeretaaetatae',user:'user',rating:5},
    {comment:'sasasapdasdasdetatae',user:'Lamus',rating:2},
  ]
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
      this.opinionData = {
        userId:this.userREST.userID,
        branchId:Number(this.branchId),
      };
      this.getOpinions();
    });
  }

  public showForm() {
    this.isForm.next(!this.isForm.value);
  }
  public onSubmit($event) {
    event.preventDefault();

    this.opinionData.comment = this.ratingForm.controls.commentText.value;
    this.opinionData.rating = this.currentRate;


    this.cDataService.postOpinion(this.opinionData).subscribe(response => {
      if(response) {
        this.sDataService.open({
          message:'Pomyślnie dodano komentarz!',
          snackbarType:SnackbarType.success,
        });
        this.isForm.next(false);
      } else {
        this.sDataService.open({
          message:'Nie udało się dodać komentarza!',
          snackbarType:SnackbarType.error,
        });
      }
    });
  }
  public getOpinions() {
    let subject = new Subject<boolean>();
    subject.subscribe(data=>{
      this.isLoaded.next(true);
    });
    this.cDataService.getOpinion(this.opinionData).subscribe(data=>{
      console.log(data);
      this.isOwner.next(this.coDataService.checkForUserPermission(Number(this.companyId)));
      subject.next(true);
    },error=>{
      subject.next(false);
    });
  }
}
