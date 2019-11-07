import { Injectable } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoaderService {
  showLoaderComponent = new BehaviorSubject<boolean>(false);
  constructor() {}

  public showLoader() {
    this.showLoaderComponent.next(true);
  }
  public hideLoader() {
    this.showLoaderComponent.next(false);
  }
}
