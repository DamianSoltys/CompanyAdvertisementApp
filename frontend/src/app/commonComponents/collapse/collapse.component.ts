import { Component, OnInit, Input } from '@angular/core';
import { Branch } from 'src/app/classes/Company';
import { BehaviorSubject } from 'rxjs';
import { trigger, style, animate, transition } from '@angular/animations';
import { CollapseService } from 'src/app/services/collapse.service';

@Component({
  selector: 'app-collapse',
  templateUrl: './collapse.component.html',
  styleUrls: ['./collapse.component.scss'],
  animations: [
    trigger(
      'collapseAnimate', [
        transition(':enter', [
          style({height: '0px',overflow:'hidden'}),
          animate('200ms', style({height: '*'}))
        ]),
        transition(':leave', [
          style({height: '*'}),
          animate('200ms', style({height: '0px',overflow:'hidden'}))
        ])
      ]
    )
  ],
})
export class CollapseComponent implements OnInit {
  @Input() branchData:Branch;
  public isOpen = new BehaviorSubject(false);
  public collapseId:number;
  constructor(private service:CollapseService) { }

  ngOnInit() {
    this.collapseId = 0;
    for(let char of this.branchData.name) {
      this.collapseId+=char.charCodeAt(0) + Math.random();
    }
    this.service.saveId.subscribe(data=>{   
      if(data === this.collapseId) {
        this.openCollapse();
      }
      else {
        this.close();
      }
    });
  }
  public open (e:Event) {
    e.preventDefault();
    this.service.open(this.collapseId);
  }
  private openCollapse() {
    if(this.isOpen.value) {
      setTimeout(()=>{
       this.close();
      },200);
     } else {
       this.isOpen.next(true);
     }
  }
  public close() {
    this.isOpen.next(false);
  }

}
