import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CommentsComponent } from './comments.component';
import { NgbRatingModule } from '@ng-bootstrap/ng-bootstrap'

@NgModule({
  declarations: [CommentsComponent],
  imports: [
    CommonModule,
    NgbRatingModule

  ],
  exports:[CommentsComponent]
})
export class CommentsModule { }
