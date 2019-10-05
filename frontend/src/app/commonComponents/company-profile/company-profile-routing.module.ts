import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CompanyProfileComponent } from './company-profile.component';

const routes: Routes = [
  {path: 'quest', component: CompanyProfileComponent},
  {path: 'owner', component: CompanyProfileComponent},
  {path: '**', redirectTo: 'quest', pathMatch: 'full'},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CompanyProfileRoutingModule { }
