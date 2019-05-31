import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { RegisterConfirmRoutingModule } from './register-confirm-routing.module';
import { RegisterConfirmComponent } from './register-confirm.component';

@NgModule({
  declarations: [RegisterConfirmComponent],
  imports: [
    CommonModule,
    RegisterConfirmRoutingModule
  ]
})
export class RegisterConfirmModule { }
