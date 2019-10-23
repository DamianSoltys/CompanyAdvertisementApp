import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SearchRoutingModule } from './search-routing.module';
import { SearchComponent } from './search.component';
import { ReactiveFormsModule } from '@angular/forms';
import { CompanyModule } from '../user/company/company.module';
import { SearchSectionComponent } from '../commonComponents/search-section/search-section.component';

@NgModule({
  declarations: [SearchComponent,SearchSectionComponent],
  imports: [CommonModule, SearchRoutingModule, ReactiveFormsModule,CompanyModule]
})
export class SearchModule {}
