import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CompanyProfileComponent } from './company-profile.component';
import { CompanyProfileRoutingModule } from './company-profile-routing.module';
import { CompanyModule } from 'src/app/user/company/company.module';
import { NewsletterModule } from '../newsletter/newsletter.module';
import { FormErrorModule } from '../form-error/form-error.module';

@NgModule({
  declarations: [
    CompanyProfileComponent,
  ],
  imports: [
    CommonModule,
    CompanyProfileRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    CompanyModule,
    NewsletterModule,
    FormErrorModule
  ]
})
export class CompanyProfileModule {}
