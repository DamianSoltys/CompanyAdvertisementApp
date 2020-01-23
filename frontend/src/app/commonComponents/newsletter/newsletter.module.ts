import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormErrorModule } from '@commonComponents/form-error/form-error.module';
import { NewsletterComponent } from './newsletter.component';
import { GrapeEditorModule } from '@commonComponents/newsletter/grape-editor/grape-editor.module';
import { QuillModule } from 'ngx-quill';
import { NgbDropdown, NgbDropdownModule, NgbDatepickerModule, NgbTimepickerModule } from '@ng-bootstrap/ng-bootstrap';
import { NewsletterListComponent } from '@commonComponents/newsletter-list/newsletter-list.component';
import { NewsletterListModule } from '@commonComponents/newsletter-list/newsletter-list.module';
import { ReactiveFormsModule } from '@angular/forms';
import { SelectDropDownModule } from 'ngx-select-dropdown';


@NgModule({
  declarations: [NewsletterComponent],
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    FormErrorModule,
    GrapeEditorModule,
    QuillModule.forRoot(),
    NgbDropdownModule,
    NewsletterListModule,
    NgbDatepickerModule,
    NgbTimepickerModule,
    SelectDropDownModule
  ],
  exports: [
    NewsletterComponent
  ]
})
export class NewsletterModule { }
