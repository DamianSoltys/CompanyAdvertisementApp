import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CollapseComponent } from './collapse.component';
import { AgmCoreModule } from '@agm/core';

@NgModule({
  declarations: [CollapseComponent],
  imports: [
    CommonModule,
    AgmCoreModule.forRoot({
      apiKey: 'AIzaSyDymmSbQ_6KBgygpEZwcztemgH3HXTOYrI'
    }),
  ],
  exports:[CollapseComponent]
})
export class CollapseModule { }
