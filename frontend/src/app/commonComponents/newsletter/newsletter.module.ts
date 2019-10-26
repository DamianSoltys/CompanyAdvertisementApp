import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormErrorModule } from '../form-error/form-error.module';
import { NewsletterComponent } from './newsletter.component';


@NgModule({
  declarations: [NewsletterComponent],
  imports: [
    CommonModule,
    RouterModule,
    FormErrorModule
  ],
  exports:[
    NewsletterComponent
  ]
})
export class NewsletterModule { }
