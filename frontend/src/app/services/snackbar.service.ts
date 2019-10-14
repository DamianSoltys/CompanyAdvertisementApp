import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { SnackbarOptions,SnackbarType, SnackbarComponent } from '../commonComponents/snackbar/snackbar.component';

@Injectable({
  providedIn: 'root'
})
export class SnackbarService {
  isOpen = new BehaviorSubject(false);
  message = new BehaviorSubject(<string>'');
  options = new BehaviorSubject(<SnackbarOptions>{
    isOpen:false,
  });
  constructor() { }

  public open(message:string,type:SnackbarType,duration?:number) {
    
    this.options.next({
      isOpen:true,
      message:message,
      snackbarType:type,
    });

    if(!duration) {
      duration = 3000;
    }

    setTimeout(()=>{
      this.close();
    },duration);
  }
  public close() {
    this.options.next({
      isOpen:false,
    });
  }
}
