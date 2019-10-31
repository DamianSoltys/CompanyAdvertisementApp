import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { NgbRatingConfig } from '@ng-bootstrap/ng-bootstrap'

@Component({
  selector: 'app-comments',
  templateUrl: './comments.component.html',
  styleUrls: ['./comments.component.scss']
})
export class CommentsComponent implements OnInit {
  public ratingForm = this.fb.group({
    commentText:[''],
    rating:[0,[Validators.required]],
  });
  public currentRate=4;
  public ratingConfig:NgbRatingConfig = {
    max:5,
    readonly:false,
    resettable:false
  }
  constructor(private fb:FormBuilder) { }

  ngOnInit() {
  }

}
