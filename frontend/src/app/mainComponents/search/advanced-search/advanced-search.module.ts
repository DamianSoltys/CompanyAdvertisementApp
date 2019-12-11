import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdvancedSearchComponent } from './advanced-search.component';
import { AdvancedSearchRoutingModule } from './advanced-search-routing.module';
import { SelectDropDownModule } from 'ngx-select-dropdown'
import { ReactiveFormsModule } from '@angular/forms';
import { SearchSectionModule } from '../search-section/search-section.module';


@NgModule({
  declarations: [AdvancedSearchComponent],
  imports: [
    CommonModule,
    AdvancedSearchRoutingModule,
    SelectDropDownModule,
    ReactiveFormsModule,
    SearchSectionModule
  ]
})
export class AdvancedSearchModule { }
