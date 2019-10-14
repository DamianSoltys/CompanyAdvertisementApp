import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SnackbarService {
  isOpen = new BehaviorSubject(false);
  message = new BehaviorSubject(<string>'');
  constructor() { }

  public open(message:string,duration?:number) {
    this.isOpen.next(true);
    this.message.next(message);

    if(!duration) {
      duration = 3000;
    }

    setTimeout(()=>{
      this.close();
    },duration);
  }
  public close() {
    this.isOpen.next(false);
  }
}
