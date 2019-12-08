import { enableProdMode } from '@angular/core';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';

import { AppModule } from './app/mainComponents/app/app.module';
import { environment } from './environments/environment';

import '../node_modules/bootstrap/dist/js/bootstrap.bundle.js';

if (environment.production) {
  enableProdMode();
}

platformBrowserDynamic().bootstrapModule(AppModule)
  .catch(err => console.error(err));
