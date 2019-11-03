import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NearbyComponent } from './nearby-component.component';
import { AgmCoreModule } from '@agm/core';

@NgModule({
  declarations: [NearbyComponent],
  imports: [
    CommonModule,
    AgmCoreModule.forRoot(),
  ],exports:[
    NearbyComponent
  ]
})
export class NearbyComponentModule { }
