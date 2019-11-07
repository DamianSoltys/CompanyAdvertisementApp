import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CommentsComponent } from './comments.component';
import { NgbRatingModule } from '@ng-bootstrap/ng-bootstrap'
import { ReactiveFormsModule } from '@angular/forms';
import { QuillModule } from 'ngx-quill';

@NgModule({
  declarations: [CommentsComponent],
  imports: [
    CommonModule,
    NgbRatingModule,
    ReactiveFormsModule,
    QuillModule

  ],
  exports:[CommentsComponent]
})
export class CommentsModule { }
