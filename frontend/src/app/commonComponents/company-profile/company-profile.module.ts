import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { QuillModule } from 'ngx-quill'
import { AgmCoreModule } from '@agm/core';
import { CompanyProfileComponent } from './company-profile.component';
import { CompanyProfileRoutingModule } from './company-profile-routing.module';

@NgModule({
  declarations: [
    CompanyProfileComponent,
  ],
  imports: [
    CommonModule,
    CompanyProfileRoutingModule,  
    FormsModule,
    ReactiveFormsModule,
    QuillModule.forRoot(),
    AgmCoreModule.forRoot({
      apiKey:'AIzaSyDymmSbQ_6KBgygpEZwcztemgH3HXTOYrI'
    }), 
  ]
})
export class CompanyProfileModule { }
