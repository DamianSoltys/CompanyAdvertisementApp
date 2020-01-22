import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

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
