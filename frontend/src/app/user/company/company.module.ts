import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CompanyRoutingModule } from './company-routing.module';
import { CompanyComponent } from './company.component';
import { CompanySectionComponent } from '../../commonComponents/company-section/company-section.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { QuillModule } from 'ngx-quill'

@NgModule({
  declarations: [
    CompanyComponent,
    CompanySectionComponent
  ],
  imports: [
    CommonModule,
    CompanyRoutingModule,  
    FormsModule,
    ReactiveFormsModule,
    QuillModule.forRoot(), 
  ]
})
export class CompanyModule { }
