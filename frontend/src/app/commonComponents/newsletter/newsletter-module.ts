import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CompanyModule } from 'src/app/user/company/company.module';
import { CommentsComponent } from '../comments/comments.component';
import { NewsletterComponent } from './newsletter.component';
import { NewsletterRoutingModule } from './newsletter-routing.module';

@NgModule({
  declarations: [
    NewsletterComponent,
  ],
  imports: [
    CommonModule,  
    FormsModule,
    ReactiveFormsModule,
    NewsletterRoutingModule,
    CompanyModule,

  ]
})
export class NewsletterModule { }
