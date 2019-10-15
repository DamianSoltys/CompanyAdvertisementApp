import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AccountDataRoutingModule } from './account-data-routing.module';
import { AccountDataComponent } from './account-data.component';
import { ReactiveFormsModule } from '@angular/forms';
import { FormErrorModule } from 'src/app/commonComponents/form-error/form-error.module';

@NgModule({
  declarations: [AccountDataComponent],
  imports: [CommonModule, AccountDataRoutingModule, ReactiveFormsModule,FormErrorModule]
})
export class AccountDataModule {}
