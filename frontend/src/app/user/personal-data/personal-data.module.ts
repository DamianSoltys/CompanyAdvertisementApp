import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PersonalDataRoutingModule } from './personal-data-routing.module';
import { PersonalDataComponent } from './personal-data.component';

@NgModule({
  declarations: [PersonalDataComponent],
  imports: [
    CommonModule,
    PersonalDataRoutingModule
  ]
})
export class PersonalDataModule { }
