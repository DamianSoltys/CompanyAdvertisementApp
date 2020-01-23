import { Component, OnInit } from '@angular/core';
import { SnackbarService, SnackbarOptions } from '@services/snackbar.service';
import { trigger, style, animate, transition } from '@angular/animations';

@Component({
  selector: 'app-snackbar',
  templateUrl: './snackbar.component.html',
  styleUrls: ['./snackbar.component.scss'],
  animations: [
    trigger(
      'snackbarAnimate', [
      transition(':enter', [
        style({ opacity: 0 }),
        animate('200ms', style({ opacity: 1 }))
      ]),
      transition(':leave', [
        style({ opacity: 1 }),
        animate('200ms', style({ opacity: 0 }))
      ])
    ]
    )
  ],
})

export class SnackbarComponent implements OnInit {
  public snackbarOptions: SnackbarOptions;

  constructor(private snackbarService: SnackbarService) { }

  ngOnInit() {
    this.snackbarService.snackbarOptions.subscribe(data => {
      this.snackbarOptions = data;
    });
  }

}
