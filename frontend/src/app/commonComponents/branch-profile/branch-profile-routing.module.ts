import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AuthGuard } from 'src/app/guards/auth-guard.service';
import { BranchProfileComponent } from './branch-profile.component';
import { CompanyComponent } from 'src/app/mainComponents/user/company/company.component';

const routes: Routes = [
  { path: '', component: BranchProfileComponent },
  { path: 'edit', component: CompanyComponent,canActivate: [AuthGuard]},
  { path: '**', redirectTo: 'guest', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BranchProfileRoutingModule {}
