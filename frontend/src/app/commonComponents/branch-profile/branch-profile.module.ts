import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BranchProfileComponent } from './branch-profile.component';
import { BranchProfileRoutingModule } from './branch-profile-routing.module';

@NgModule({
  declarations: [
    BranchProfileComponent
  ],
  imports: [
    CommonModule,
    BranchProfileRoutingModule,
    FormsModule,
    ReactiveFormsModule,
  ]
})
export class BranchProfileModule { }
