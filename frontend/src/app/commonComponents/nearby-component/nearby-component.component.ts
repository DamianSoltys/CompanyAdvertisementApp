import { Component, OnInit, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-nearby-component',
  templateUrl: './nearby-component.component.html',
  styleUrls: ['./nearby-component.component.scss']
})
export class NearbyComponent implements OnInit {
  @Input() name;
  constructor(public activeModal: NgbActiveModal) { }

  ngOnInit() {
  }

}
