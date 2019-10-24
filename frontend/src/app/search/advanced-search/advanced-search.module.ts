import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdvancedSearchComponent } from './advanced-search.component';
import { AdvancedSearchRoutingModule } from './advanced-search-routing.module';

@NgModule({
  declarations: [AdvancedSearchComponent],
  imports: [
    CommonModule,
    AdvancedSearchRoutingModule
  ]
})
export class AdvancedSearchModule { }
