import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FavouriteBranchesComponent } from './favourite-branches.component';
import { FavouriteBranchesRoutingModule } from './favourite-routing-module';

@NgModule({
  declarations: [FavouriteBranchesComponent],
  imports: [
    CommonModule,
    FavouriteBranchesRoutingModule
  ],exports:[
    FavouriteBranchesComponent
  ]
})
export class FavouriteBranchesModule { }
