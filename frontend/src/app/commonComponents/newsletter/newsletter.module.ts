import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormErrorModule } from '../form-error/form-error.module';
import { NewsletterComponent } from './newsletter.component';
import { GrapeEditorModule } from './grape-editor/grape-editor.module';
import { QuillModule } from 'ngx-quill';


@NgModule({
  declarations: [NewsletterComponent],
  imports: [
    CommonModule,
    RouterModule,
    FormErrorModule,
    GrapeEditorModule,
    QuillModule.forRoot(),
  ],
  exports:[
    NewsletterComponent
  ]
})
export class NewsletterModule { }