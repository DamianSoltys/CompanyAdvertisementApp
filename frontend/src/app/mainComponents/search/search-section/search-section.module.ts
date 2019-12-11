import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SearchSectionComponent } from './search-section.component';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [SearchSectionComponent],
  imports: [
    CommonModule,
    RouterModule
  ],
  exports:[
    SearchSectionComponent
  ]
})
export class SearchSectionModule { }
