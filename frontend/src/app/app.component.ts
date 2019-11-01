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
  transition,
  query
} from '@angular/animations';
import { BehaviorSubject, Observable } from 'rxjs';
import { LoginService } from './services/login.service';
import { Router, RouterOutlet } from '@angular/router';
import { storage_Avaliable } from './classes/storage_checker';
import { UserREST, PersonalData } from './classes/User';
import { PersonalDataService } from './services/personal-data.service';
import { SnackbarService, SnackbarType } from './services/snackbar.service';
import { slideInAnimation } from './animations/route-animation';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NearbyComponent } from './commonComponents/nearby-component/nearby-component.component';

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
          height:'*',
          overflow:'hidden'
        })
      ),
      state(
        'hidden',
        style({
          height:'0px',
          overflow:'hidden'
        })
      ),
      transition('visible <=> hidden', [animate('.2s')])
    ]),
    slideInAnimation,
  ]
})
export class AppComponent implements OnInit {
  public nearby_toggle = false;
  public logged = false;
  public visible = true;
  public logOut_success = false;
  public displayPersonalDataMenu = new BehaviorSubject(false);
  public displaySearchMenu = new BehaviorSubject(false);
  public displayHamburgerMenu = new BehaviorSubject(false);
  public userREST: UserREST;
  public personalData: PersonalData;

  constructor(
    private lgservice: LoginService,
    private pDataService: PersonalDataService,
    private snackbarService:SnackbarService,
    private modalService: NgbModal,
  ) {
    document.body.addEventListener('click', e => {
      if ((<HTMLElement>e.target).id !== 'menuId') {
        this.displayHamburgerMenu.next(false);
      }
    });
  }

  ngOnInit(): void {
    this.lgservice.Logged.subscribe(value => {
      this.logged = value;
      this.getStorageObjects();
    });
  }

  public openNearbyModal() {
    const modalRef = this.modalService.open(NearbyComponent,{size:'lg'});
    modalRef.componentInstance.name = 'World';
  }

  public getStorageObjects() {
    if (storage_Avaliable('localStorage') && this.logged) {
      this.userREST = JSON.parse(localStorage.getItem('userREST'));
      this.pDataService.personalData.subscribe(data => {
        this.personalData = data;
      });
    }
  }

  public toggleNearbyBlock() {
    this.nearby_toggle = !this.nearby_toggle;
    this.visible = !this.visible;
  }
  public toggleMenu() {   
      this.displayHamburgerMenu.next(!this.displayHamburgerMenu.value);
  }

  public logOut() {
    this.logOut_success = true;
    this.snackbarService.open({
      message:'Pomyślnie wylogowano',
      snackbarType:SnackbarType.success,
    });
    this.lgservice.logoutStorageClean();
  }

  public prepareRoute(outlet: RouterOutlet) {
    return outlet && outlet.activatedRouteData;
  }
}
