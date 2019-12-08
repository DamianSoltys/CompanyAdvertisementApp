import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { NewsletterAuthComponent } from './newsletter-auth.component';

const routes: Routes = [{ path: '', component: NewsletterAuthComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class NewsletterAuthRoutingModule {}
