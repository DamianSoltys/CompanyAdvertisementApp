import {
  Component,
  OnInit,
  AfterContentChecked,
  Renderer2,
  ViewChild,
  ElementRef
} from '@angular/core';
import {
  trigger,
  state,
  style,
  animate,
  transition
} from '@angular/animations';
import { BehaviorSubject, Observable } from 'rxjs';
import { LoginService } from './services/login.service';
import { Router } from '@angular/router';
import { storage_Avaliable } from './classes/storage_checker';
import { UserREST, PersonalData } from './classes/User';
import { PersonalDataService } from './services/personal-data.service';
import { SnackbarService } from './services/snackbar.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  animations: [
    trigger('showHide', [
      state(
        'visible',
        style({
          height: '100px',
          opacity: '1',
          visibility: 'visible'
        })
      ),
      state(
        'hidden',
        style({
          height: '0',
          opacity: '0',
          visibility: 'hidden'
        })
      ),
      transition('visible <=> hidden', [animate('0.5s')])
    ]),
    trigger('showHideMenu', [
      state(
        'visible',
        style({
          transform: 'scaleY(1)',
          top: '48px'
        })
      ),
      state(
        'hidden',
        style({
          transform: 'scaleY(0)',
          top: '-200px'
        })
      ),
      transition('visible <=> hidden', [animate('.2s')])
    ]),
    trigger('showHideDropdown', [
      state(
        'visible',
        style({
          transform: 'scaleY(1)'
        })
      ),
      state(
        'hidden',
        style({
          transform: 'scaleY(0)'
        })
      ),
      transition('visible <=> hidden', [animate('.2s')])
    ])
  ]
})
export class AppComponent implements OnInit {
  @ViewChild('logOutMessage', { read: ElementRef }) logOutMessage: ElementRef;
  public nearby_toggle = false;
  public logged = false;
  public visible = true;
  public logOut_success = false;
  public displayHamburgerMenu = new BehaviorSubject(false);
  public displayDropdown = new BehaviorSubject(false);
  public userREST: UserREST;
  public personalData: PersonalData;

  constructor(
    private lgservice: LoginService,
    private router: Router,
    private renderer: Renderer2,
    private pDataService: PersonalDataService,
    private snackbarService:SnackbarService
  ) {
    document.body.addEventListener('click', e => {
      if ((<HTMLElement>e.target).id !== 'menuId') {
        this.displayHamburgerMenu.next(false);
      }
      if ((<HTMLElement>e.target).id !== 'dropdownId') {
        this.displayDropdown.next(false);
      }
    });
  }

  ngOnInit(): void {
    this.lgservice.Logged.subscribe(value => {
      this.logged = value;
      this.getStorageObjects();
    });
  }

  getStorageObjects() {
    if (storage_Avaliable('localStorage') && this.logged) {
      this.userREST = JSON.parse(localStorage.getItem('userREST'));
      this.pDataService.personalData.subscribe(data => {
        this.personalData = data;
      });
    }
  }

  toggleNearbyBlock() {
    this.nearby_toggle = !this.nearby_toggle;
    this.visible = !this.visible;
  }
  toggleMenu(mobile?: boolean) {
    if (!mobile) {
      this.displayDropdown.next(!this.displayDropdown.value);
    } else {
      this.displayHamburgerMenu.next(!this.displayHamburgerMenu.value);
    }
  }

  logOut() {
    this.logOut_success = true;
    this.logOutMessageRender();
    this.lgservice.logoutStorageClean();
    console.log('wylogowany');
  }

  logOutMessageRender() {
    this.renderer.setStyle(
      this.logOutMessage.nativeElement,
      'visibility',
      'visible'
    );
    setTimeout(() => {
      this.logOut_success = false;
      this.renderer.setStyle(
        this.logOutMessage.nativeElement,
        'visibility',
        'hidden'
      );
    }, 1000);
  }
}
