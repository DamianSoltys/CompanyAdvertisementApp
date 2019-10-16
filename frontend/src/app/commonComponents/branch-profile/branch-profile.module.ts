import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BranchProfileComponent } from './branch-profile.component';
import { BranchProfileRoutingModule } from './branch-profile-routing.module';
import { AgmCoreModule } from '@agm/core';
import { CommentsComponent } from '../comments/comments.component';
import { CompanyModule } from 'src/app/user/company/company.module';
@NgModule({
  declarations: [BranchProfileComponent, CommentsComponent],
  imports: [
    CommonModule,
    BranchProfileRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    AgmCoreModule.forRoot({
      apiKey: 'AIzaSyDymmSbQ_6KBgygpEZwcztemgH3HXTOYrI'
    }),
    CompanyModule
  ]
})
export class BranchProfileModule {}
