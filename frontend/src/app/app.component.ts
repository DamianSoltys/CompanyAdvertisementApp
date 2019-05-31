import { Component, OnInit, AfterContentChecked } from '@angular/core';
import { trigger, state, style, animate, transition } from '@angular/animations';
import { BehaviorSubject, Observable } from 'rxjs';
import { LoginService } from './services/login.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  animations: [
    trigger('showHide', [
      state('visible', style({
        opacity: '1',
        display: 'block'

      })),
      state('hidden', style({
        opacity: '0',
        display: 'none'

      })),
      transition('visible <=> hidden', [
        animate('1s')
      ]),

    ]),
    trigger('showHideMenu', [
      state('visible', style({
      opacity: '1',
      display: 'block'


      })),
      state('hidden', style({
        opacity: '0',
        display: 'none'


      })),
      transition('visible <=> hidden', [
        animate('0.5s')
      ]),

    ]),
  ],
})
export class AppComponent implements OnInit {
  nearby_toggle = false;
  logged = false;
  visible = true;
  displayMenu = new BehaviorSubject(false);
  constructor(private lgservice: LoginService, private router: Router) {
    document.body.addEventListener('click', (e) => {
     if ((<HTMLElement>e.target).id !== 'menuId') {
       this.displayMenu.next(false);
     }

    });
  }
  ngOnInit(): void {
    this.lgservice.Logged.subscribe(value => {
    this.logged = value;
    });
  }

  toggleNearbyBlock() {
    this.nearby_toggle = !this.nearby_toggle;
    this.visible = !this.visible;
  }
  toggleMenu() {
    this.displayMenu.next(!this.displayMenu.value);
  }
  logOut() {
    console.log(localStorage.getItem('token'));
    localStorage.removeItem('token');
    this.lgservice.ChangeLogged();
    this.router.navigate(['']);
    console.log('wylogowany');
  }
}
