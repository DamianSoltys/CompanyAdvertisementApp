import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NewsletterListComponent } from './newsletter-list.component';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [NewsletterListComponent],
  imports: [
    CommonModule,
    RouterModule
  ],exports:[
    NewsletterListComponent
  ]
})
export class NewsletterListModule { }
