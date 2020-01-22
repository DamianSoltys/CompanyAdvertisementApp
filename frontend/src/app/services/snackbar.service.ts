import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export enum SnackbarType {
  error,
  success,
  information
}

export interface SnackbarOptions {
  snackbarType?: SnackbarType,
  message?: string,
  isOpen?: boolean,
  duration?: number,
}

@Injectable({
  providedIn: 'root'
})
export class SnackbarService {
  public snackbarOptions = new BehaviorSubject(<SnackbarOptions>{
    isOpen: false,
  });

  constructor() { }

  public open(snackbarOptions: SnackbarOptions) {
    if (this.snackbarOptions.value.isOpen) {
      this.close();
      setTimeout(() => {
        this.open(snackbarOptions);
      }, 200);
    } else {
      this.snackbarOptions.next({
        isOpen: true,
        message: snackbarOptions.message,
        snackbarType: snackbarOptions.snackbarType,
      });

      if (!snackbarOptions.duration) {
        snackbarOptions.duration = 3000;
      }

      setTimeout(() => {
        this.close();
      }, snackbarOptions.duration);
    }
  }
  public close() {
    this.snackbarOptions.next({
      isOpen: false,
    });
  }
}
