import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FavouriteBranchesComponent } from './favourite-branches.component';
import { FavouriteBranchesRoutingModule } from './favourite-routing-module';
import { NgbRatingModule } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
  declarations: [FavouriteBranchesComponent],
  imports: [
    CommonModule,
    FavouriteBranchesRoutingModule,
    NgbRatingModule
  ],exports:[
    FavouriteBranchesComponent
  ]
})
export class FavouriteBranchesModule { }
