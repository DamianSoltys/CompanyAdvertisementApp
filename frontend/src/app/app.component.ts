import { Component, OnInit } from '@angular/core';

import * as $ from 'jquery';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  nearby_toggle = false;
  logged = false;
ngOnInit(): void {
}

menuAnimation() {
  $('.dropdown-menu').slideToggle( 'slow' , () => {
   $(document).ready(() => {
     $(document).on('click', () => {
      $('.dropdown-menu').slideUp('slow', () => {

      });
     });
   });
  });
}
toggleNearby() {
  this.nearby_toggle = !this.nearby_toggle;
  $('.nearby').slideToggle('slow');
}

}
