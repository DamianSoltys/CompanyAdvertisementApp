import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CompanyRoutingModule } from './company-routing.module';
import { CompanyComponent } from './company.component';
import { CompanySectionComponent } from '../../commonComponents/company-section/company-section.component';

@NgModule({
  declarations: [
    CompanyComponent,
    CompanySectionComponent
  ],
  imports: [
    CommonModule,
    CompanyRoutingModule,    
  ]
})
export class CompanyModule { }
