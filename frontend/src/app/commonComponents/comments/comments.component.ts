import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';

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
  
  constructor(private fb:FormBuilder) { }

  ngOnInit() {
  }

}
