import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AuthGuard } from 'src/app/guards/auth-guard.service';
import { BranchProfileComponent } from './branch-profile.component';
import { CommentsComponent } from '../comments/comments.component';
import { CompanyComponent } from 'src/app/user/company/company.component';

const routes: Routes = [
  { path: '', component: BranchProfileComponent },
  { path: 'comments', component: CommentsComponent },
  { path: 'edit', component: CompanyComponent,canActivate: [AuthGuard]},
  { path: '**', redirectTo: 'guest', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BranchProfileRoutingModule {}
