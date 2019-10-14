import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CompanyProfileComponent } from './company-profile.component';
import { AuthGuard } from 'src/app/guards/auth-guard.service';
import { NewsletterComponent } from '../newsletter/newsletter.component';

const routes: Routes = [
  { path: '', component: CompanyProfileComponent },
  { path: '**', redirectTo: 'guest', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CompanyProfileRoutingModule {}
