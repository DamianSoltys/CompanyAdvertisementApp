import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NewsletterAuthRoutingModule } from './newsletter-auth-routing.module';
import { NewsletterAuthComponent } from './newsletter-auth.component';


@NgModule({
  declarations: [NewsletterAuthComponent],
  imports: [
    CommonModule,
    NewsletterAuthRoutingModule
  ]
})
export class NewsletterAuthModule { }
