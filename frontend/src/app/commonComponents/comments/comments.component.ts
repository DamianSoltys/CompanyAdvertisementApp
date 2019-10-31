import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';

import { BehaviorSubject } from 'rxjs';
import {
  trigger,
  state,
  style,
  animate,
  transition,
  query
} from '@angular/animations';
import { NgbRatingConfig } from '@ng-bootstrap/ng-bootstrap'


@Component({
  selector: 'app-comments',
  templateUrl: './comments.component.html',
  styleUrls: ['./comments.component.scss'],
  animations: [
    trigger(
      'formAnimate', [
        transition(':enter', [
          style({opacity: 0}),
          animate('500ms', style({visibility:'visible',opacity:1}))
        ]),
        transition(':leave', [
          style({opacity: 1}),
          animate('500ms', style({visibility:'hidden',opacity:0}))
        ])
      ]
    )
  ],
})
export class CommentsComponent implements OnInit {
  public ratingForm = this.fb.group({
    commentText:[''],
    rating:[0,[Validators.required]],
  });
  public isForm=new BehaviorSubject(false);
  public currentRate=4;
  public ratingConfig:NgbRatingConfig = {
    max:5,
    readonly:false,
    resettable:false
  }
  constructor(private fb:FormBuilder) { }

  ngOnInit() {
  }

  public showForm() {
    this.isForm.next(!this.isForm.value);
  }

}
