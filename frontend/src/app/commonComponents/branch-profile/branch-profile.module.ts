import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BranchProfileComponent } from './branch-profile.component';
import { BranchProfileRoutingModule } from './branch-profile-routing.module';
import { AgmCoreModule } from '@agm/core';
@NgModule({
  declarations: [BranchProfileComponent],
  imports: [
    CommonModule,
    BranchProfileRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    AgmCoreModule.forRoot({
      apiKey: 'AIzaSyDymmSbQ_6KBgygpEZwcztemgH3HXTOYrI'
    })
  ]
})
export class BranchProfileModule {}
