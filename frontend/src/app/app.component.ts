import { Component, OnInit, AfterContentChecked, Renderer2 , ViewChild, ElementRef } from '@angular/core';
import { trigger, state, style, animate, transition } from '@angular/animations';
import { BehaviorSubject, Observable } from 'rxjs';
import { LoginService } from './services/login.service';
import { Router } from '@angular/router';
import { storage_Avaliable } from './classes/storage_checker';

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
        transform:'scaleY(1)',
        top:'48px'
      })),
      state('hidden', style({
        transform:'scaleY(0)',
        top:'-200px'
      })),
      transition('visible <=> hidden', [
        animate('.2s')
      ]),

    ]),
  ],
})
export class AppComponent implements OnInit {
  @ViewChild('logOutMessage', {read: ElementRef}) logOutMessage: ElementRef;
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
    this.logOutMessageRender();
    this.logoutStorageClean();
    this.lgservice.ChangeLogged();
    this.router.navigate(['']);
    console.log('wylogowany');
  }

  logoutStorageClean() {
    if(storage_Avaliable('localStorage')) {
      localStorage.removeItem('token');
      localStorage.removeItem('userREST');
    } else {
      console.log('Storage nie jest dostępny')
    }
  }

  logOutMessageRender() {
    this.renderer.setStyle(this.logOutMessage.nativeElement, 'visibility', 'visible');
    setTimeout(() => {
      this.logOut_success = false;
      this.renderer.setStyle(this.logOutMessage.nativeElement, 'visibility', 'hidden');
    }, 1000);
  }
}
