import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { HttpConfigInterceptor } from '../../interceptor/http.interceptor';
import { LoaderComponent } from '../../commonComponents/loader/loader.component';
import { SnackbarModule } from '../../commonComponents/snackbar/snackbar.module';
import { NgbDropdown, NgbDropdownModule, NgbModalModule } from '@ng-bootstrap/ng-bootstrap';
import { NearbyComponentModule } from '../../commonComponents/nearby-component/nearby-component.module';
import { NearbyComponent } from '../../commonComponents/nearby-component/nearby-component.component';


@NgModule({
  declarations: [
    AppComponent,
    LoaderComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    SnackbarModule,
    NgbDropdownModule,
    NgbModalModule,
    NearbyComponentModule
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: HttpConfigInterceptor, multi: true}
  ],
  bootstrap: [AppComponent],
  entryComponents:[
    NearbyComponent
  ]

})
export class AppModule { }
