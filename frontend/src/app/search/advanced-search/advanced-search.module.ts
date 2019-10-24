import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdvancedSearchComponent } from './advanced-search.component';
import { AdvancedSearchRoutingModule } from './advanced-search-routing.module';
import { SelectDropDownModule } from 'ngx-select-dropdown'
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [AdvancedSearchComponent],
  imports: [
    CommonModule,
    AdvancedSearchRoutingModule,
    SelectDropDownModule,
    ReactiveFormsModule
  ]
})
export class AdvancedSearchModule { }
