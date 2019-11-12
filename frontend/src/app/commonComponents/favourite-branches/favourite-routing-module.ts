import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AuthGuard } from 'src/app/guards/auth-guard.service';
import { FavouriteBranchesComponent } from './favourite-branches.component';

const routes: Routes = [
  { path: '', component: FavouriteBranchesComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FavouriteBranchesRoutingModule {}
