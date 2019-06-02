import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PersonalDataRoutingModule } from './personal-data-routing.module';
import { PersonalDataComponent } from './personal-data.component';
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [PersonalDataComponent],
  imports: [
    CommonModule,
    PersonalDataRoutingModule,
    ReactiveFormsModule
  ]
})
export class PersonalDataModule { }
