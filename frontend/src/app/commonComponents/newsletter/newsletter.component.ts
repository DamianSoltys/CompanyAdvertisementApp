import { Component, OnInit, AfterViewInit } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { NewsletterService } from 'src/app/services/newsletter.service';
import { UserREST } from 'src/app/classes/User';
import { storage_Avaliable } from 'src/app/classes/storage_checker';
import { Router, ActivatedRoute } from '@angular/router';
import { CompanyService } from 'src/app/services/company.service';
enum FormType {
  info,
  promotion,
  product,
  text
}
@Component({
  selector: 'app-newsletter',
  templateUrl: './newsletter.component.html',
  styleUrls: ['./newsletter.component.scss']
})
export class NewsletterComponent implements OnInit{
  public isInfo = new BehaviorSubject(false);
  public isPromotion = new BehaviorSubject(false);
  public isProduct = new BehaviorSubject(false);
  public isText = new BehaviorSubject(false);
  public type:FormType;
  public typeButton:string = 'Wersja tekstowa';
  public config = {
    toolbar: [['bold', 'italic', 'underline']]
  };
  public userREST:UserREST;
  public paramId:number
  constructor(private nDataService:NewsletterService,private route:ActivatedRoute,private cDataService:CompanyService,private router:Router) {}

  ngOnInit() {
   this.setFirstFocused();
   this.getActualUser();
   this.checkForPermissions();
  }

  public getActualUser() {
    if(storage_Avaliable('localStorage')) {
      if(localStorage.getItem('userREST')) {
        this.userREST = JSON.parse(localStorage.getItem('userREST'));
      }
    }
  }

  private checkForPermissions() {
    this.route.parent.params.subscribe(params=>{
      this.paramId = params['id'];
      if(!this.cDataService.checkForUserPermission(this.paramId)) {
        this.router.navigate(['companyProfile',this.paramId]);
      }
    });
  }

  public setFirstFocused() {
    let buttons:HTMLCollection = document.getElementsByClassName('btn-newsletter');
    buttons.item(0).classList.add('button-type--focus');
    this.nDataService.destroyEditor.next(true);
    this.isInfo.next(true);
    this.isProduct.next(false);
    this.isPromotion.next(false);
    this.isText.next(false);
    this.type = FormType.info;
    this.typeButton = 'Wersja tekstowa';
  }

  public showInfo($event?:Event) {
    this.setFocusedButton(event);
    this.nDataService.destroyEditor.next(true);
    this.isInfo.next(true);
    this.isProduct.next(false);
    this.isPromotion.next(false);
    this.isText.next(false);
    this.type = FormType.info;
    this.typeButton = 'Wersja tekstowa';
  }
  
  public showProduct($event?:Event) {
    this.setFocusedButton(event);
    this.nDataService.destroyEditor.next(true);
    this.isInfo.next(false);
    this.isProduct.next(true);
    this.isPromotion.next(false);
    this.isText.next(false);
    this.type = FormType.product;
    this.typeButton = 'Wersja tekstowa';
  }

  public showPromotion($event?:Event) {
    this.setFocusedButton(event);
    this.nDataService.destroyEditor.next(true);
    this.isInfo.next(false);
    this.isProduct.next(false);
    this.isPromotion.next(true);
    this.isText.next(false);
    this.type = FormType.promotion;
    this.typeButton = 'Wersja tekstowa';
  }

  public showTextForm(type:FormType) {
    if(this.isText.value) {
      this.isText.next(false);
      this.typeButton = 'Wersja Tekstowa';
      console.log(type);
    } else {
      this.typeButton = 'Edytor';
      this.isText.next(true);
      console.log(type);
    }
  }

  public showTextButton() {
    if(this.isProduct.value || this.isPromotion.value || this.isInfo.value) {
      return true;
    } else {
      return false;
    }
  }

  private setFocusedButton($event) {
    let buttons:HTMLCollection = document.getElementsByClassName('btn-newsletter');
    for(let i=0;i<buttons.length;i++) {
      if(buttons.item(i).classList.contains('button-type--focus')) {
        buttons.item(i).classList.remove('button-type--focus')
      }
    }
    
    let element:HTMLElement = <HTMLElement>event.currentTarget;
    element.classList.add('button-type--focus');
  }
  

}
