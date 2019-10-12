import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AuthGuard } from 'src/app/guards/auth-guard.service';
import { BranchProfileComponent } from './branch-profile.component';

const routes: Routes = [
  {path: 'owner', component: BranchProfileComponent,
  data:{owner:'true'},
  canActivate: [AuthGuard]},
  {path: 'guest', component: BranchProfileComponent},
  {path: '**', redirectTo: 'guest', pathMatch: 'full'},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BranchProfileRoutingModule { }
