import { Component, OnInit } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { SnackbarService, SnackbarOptions } from 'src/app/services/snackbar.service';
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
          animate('200ms', style({opacity: 1}))
        ]),
        transition(':leave', [
          style({opacity: 1}),
          animate('200ms', style({opacity: 0}))
        ])
      ]
    )
  ],
})

export class SnackbarComponent implements OnInit {
  options:SnackbarOptions;
  constructor(private snackbarService:SnackbarService) { }

  ngOnInit() {
    this.snackbarService.options.subscribe(data=>{
      this.options = data;
    });
  }

}
