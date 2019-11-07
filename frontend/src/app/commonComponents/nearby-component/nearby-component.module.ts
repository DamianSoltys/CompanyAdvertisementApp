import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NearbyComponent } from './nearby-component.component';
import { AgmCoreModule } from '@agm/core';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [NearbyComponent],
  imports: [
    CommonModule,
    RouterModule,
    AgmCoreModule.forRoot({
      apiKey: 'AIzaSyDymmSbQ_6KBgygpEZwcztemgH3HXTOYrI'
    }),
  ],exports:[
    NearbyComponent
  ]
})
export class NearbyComponentModule { }
