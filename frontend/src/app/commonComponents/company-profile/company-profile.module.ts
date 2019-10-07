import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CompanyProfileComponent } from './company-profile.component';
import { CompanyProfileRoutingModule } from './company-profile-routing.module';
import { CompanyModule } from 'src/app/user/company/company.module';
import { CommentsComponent } from '../comments/comments.component';

@NgModule({
  declarations: [
    CompanyProfileComponent,
    CommentsComponent
  ],
  imports: [
    CommonModule,
    CompanyProfileRoutingModule,  
    FormsModule,
    ReactiveFormsModule,
    CompanyModule,

  ]
})
export class CompanyProfileModule { }
