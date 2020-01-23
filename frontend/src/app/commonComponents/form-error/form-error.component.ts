import { Component, OnInit } from '@angular/core';
import { trigger, style, animate, transition } from '@angular/animations';
import { FormErrorOptions, FormErrorService } from '@services/form-error.service';

@Component({
  selector: 'app-form-error',
  templateUrl: './form-error.component.html',
  styleUrls: ['./form-error.component.scss'],
  animations: [
    trigger(
      'errorAnimate', [
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

export class FormErrorComponent implements OnInit {
  public formErrorOptions: FormErrorOptions;

  constructor(private formErrorService: FormErrorService) { }

  ngOnInit() {
    document.body.addEventListener('click', e => {
      if (this.formErrorOptions.isOpen) {
        this.formErrorService.close();
      }
    });
    this.formErrorService.formErrorOptions.subscribe(data => {
      this.formErrorOptions = data;
    });
  }

}
