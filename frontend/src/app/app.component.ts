import { Component, OnInit, AfterContentChecked, Renderer2 , ViewChild, ElementRef } from '@angular/core';
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
        height: '100px',
        opacity: '1',
        visibility: 'visible'
      })),
      state('hidden', style({
       height: '0',
       opacity: '0',
       visibility: 'hidden'
      })),
      transition('visible <=> hidden', [
        animate('0.5s')
      ]),

    ]),
    trigger('showHideMenu', [
      state('visible', style({
      opacity: '1',
      visibility: 'visible'


      })),
      state('hidden', style({
        opacity: '0',
        visibility: 'hidden'


      })),
      transition('visible <=> hidden', [
        animate('0.4s')
      ]),

    ]),
  ],
})
export class AppComponent implements OnInit {
  @ViewChild('logOutMessage', {static: false}) logOutMessage: ElementRef;
  nearby_toggle = false;
  logged = false;
  visible = true;
  logOut_success = false;
  displayMenu = new BehaviorSubject(false);
  constructor(private lgservice: LoginService, private router: Router, private renderer: Renderer2) {
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
    this.logOut_success = true;
    this.renderer.setStyle(this.logOutMessage.nativeElement, 'display', 'block');
    setTimeout(() => {
      this.logOut_success = false;
      this.renderer.setStyle(this.logOutMessage.nativeElement, 'display', 'none');
    }, 1000);
    localStorage.removeItem('token');
    this.lgservice.ChangeLogged();
    this.router.navigate(['']);
    console.log('wylogowany');
  }
}
