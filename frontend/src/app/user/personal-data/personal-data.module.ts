import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PersonalDataRoutingModule } from './personal-data-routing.module';
import { PersonalDataComponent } from './personal-data.component';
import { ReactiveFormsModule } from '@angular/forms';
import { FormErrorModule } from 'src/app/commonComponents/form-error/form-error.module';

@NgModule({
  declarations: [PersonalDataComponent],
  imports: [CommonModule, PersonalDataRoutingModule, ReactiveFormsModule,FormErrorModule]
})
export class PersonalDataModule {}
