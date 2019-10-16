import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CompanyProfileComponent } from './company-profile.component';
import { CompanyProfileRoutingModule } from './company-profile-routing.module';
import { CompanyModule } from 'src/app/user/company/company.module';
import { NewsletterComponent } from '../newsletter/newsletter.component';

@NgModule({
  declarations: [
    CompanyProfileComponent,
    NewsletterComponent
  ],
  imports: [
    CommonModule,
    CompanyProfileRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    CompanyModule
  ]
})
export class CompanyProfileModule {}
