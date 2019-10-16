import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { timingSafeEqual } from 'crypto';

@Injectable({
  providedIn: 'root'
})
export class CollapseService {
  public saveId = new BehaviorSubject<number>(null);
  public isOpen = new BehaviorSubject<boolean>(null);
  constructor() { }

  public open(collapseId) {
    this.saveId.next(collapseId);
  }
}
