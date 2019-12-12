import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BranchProfileComponent } from './branch-profile.component';
import { BranchProfileRoutingModule } from './branch-profile-routing.module';
import { AgmCoreModule } from '@agm/core';
import { CompanyModule } from 'src/app/mainComponents/user/company/company.module';
import { CommentsModule } from '../comments/comments.module';

@NgModule({
  declarations: [BranchProfileComponent],
  imports: [
    CommonModule,
    BranchProfileRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    AgmCoreModule.forRoot({
      apiKey: 'xxx'
    }),
    CompanyModule,
    CommentsModule
  ]
})
export class BranchProfileModule {}
