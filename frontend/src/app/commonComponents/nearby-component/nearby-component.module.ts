import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NearbyComponent } from './nearby-component.component';
import { AgmCoreModule} from '@agm/core';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [NearbyComponent],
  imports: [
    CommonModule,
    RouterModule,
    AgmCoreModule.forRoot({
      apiKey: 'xxx'
    }),
  ],exports:[
    NearbyComponent
  ]
})
export class NearbyComponentModule { }
