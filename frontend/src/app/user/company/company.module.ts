import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CompanyRoutingModule } from './company-routing.module';
import { CompanyComponent } from './company.component';
import { CompanySectionComponent } from '../../commonComponents/company-section/company-section.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { QuillModule } from 'ngx-quill';
import { AgmCoreModule } from '@agm/core';
import { FormErrorModule } from 'src/app/commonComponents/form-error/form-error.module';
import { CollapseComponent } from 'src/app/commonComponents/collapse/collapse.component';
import { SelectDropDownModule } from 'ngx-select-dropdown';

@NgModule({
  declarations: [CompanyComponent, CompanySectionComponent,CollapseComponent],
  imports: [
    CommonModule,
    CompanyRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    QuillModule.forRoot(),
    AgmCoreModule.forRoot({
      apiKey: 'AIzaSyDymmSbQ_6KBgygpEZwcztemgH3HXTOYrI'
    }),
    FormErrorModule,
    SelectDropDownModule
  ],
  exports: [CompanyComponent, CompanySectionComponent]
})
export class CompanyModule {}
