import { Component, OnInit } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { SnackbarService } from 'src/app/services/snackbar.service';
import { trigger, style, animate, transition } from '@angular/animations';

@Component({
  selector: 'app-snackbar',
  templateUrl: './snackbar.component.html',
  styleUrls: ['./snackbar.component.scss'],
  animations: [
    trigger(
      'snackbarAnimate', [
        transition(':enter', [
          style({opacity: 0}),
          animate('500ms', style({opacity: 1}))
        ]),
        transition(':leave', [
          style({opacity: 1}),
          animate('500ms', style({opacity: 0}))
        ])
      ]
    )
  ],
})
export class SnackbarComponent implements OnInit {
  message:string;
  isOpen = new BehaviorSubject(false);

  constructor(private snackbarService:SnackbarService) { }

  ngOnInit() {
    this.snackbarService.message.subscribe(data=>{
      this.message = data;
    });
    this.snackbarService.isOpen.subscribe(data=>{
      this.isOpen.next(data);
    });
  }

}
