import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CollapseComponent } from './collapse.component';
import { AgmCoreModule } from '@agm/core';

@NgModule({
  declarations: [CollapseComponent],
  imports: [
    CommonModule,
    AgmCoreModule.forRoot({
      apiKey: 'xxx'
    }),
  ],
  exports:[CollapseComponent]
})
export class CollapseModule { }
