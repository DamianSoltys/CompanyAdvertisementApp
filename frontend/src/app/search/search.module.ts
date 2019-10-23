import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SearchRoutingModule } from './search-routing.module';
import { SearchComponent } from './search.component';
import { ReactiveFormsModule } from '@angular/forms';
import { CompanyModule } from '../user/company/company.module';

@NgModule({
  declarations: [SearchComponent],
  imports: [CommonModule, SearchRoutingModule, ReactiveFormsModule,CompanyModule]
})
export class SearchModule {}
