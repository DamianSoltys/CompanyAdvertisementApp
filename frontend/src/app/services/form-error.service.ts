import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface FormErrorOptions {
  isOpen?: boolean;
  message?: string;
  duration?: number;
}

@Injectable({
  providedIn: 'root'
})
export class FormErrorService {
  public formErrorOptions = new BehaviorSubject(<FormErrorOptions>{
    isOpen: false
  });

  constructor() {}

  public open(formErrorOptions: FormErrorOptions) {
    if (this.formErrorOptions.value.isOpen) {
      this.close();

      setTimeout(() => {
        this.open(formErrorOptions);
      }, 200);
    } else {
      this.formErrorOptions.next({
        isOpen: true,
        message: formErrorOptions.message
      });
      if (formErrorOptions.duration) {
        setTimeout(() => {
          this.close();
        }, formErrorOptions.duration);
      }
    }
  }
  public close() {
    this.formErrorOptions.next({
      isOpen: false
    });
  }
}
