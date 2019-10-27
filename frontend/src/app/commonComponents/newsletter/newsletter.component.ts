import { Component, OnInit, AfterViewInit } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { NewsletterService } from 'src/app/services/newsletter.service';
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
  constructor(private nDataService:NewsletterService) {}

  ngOnInit() {
    
  }

  public showInfo($event:Event) {
    this.setFocusedButton(event)
    this.nDataService.destroyEditor.next(true);
    this.isInfo.next(true);
    this.isProduct.next(false);
    this.isPromotion.next(false);
    this.isText.next(false);
    this.type = FormType.info;
    this.typeButton = 'Wersja tekstowa';
  }
  
  public showProduct($event) {
    this.setFocusedButton(event)
    this.nDataService.destroyEditor.next(true);
    this.isInfo.next(false);
    this.isProduct.next(true);
    this.isPromotion.next(false);
    this.isText.next(false);
    this.type = FormType.product;
    this.typeButton = 'Wersja tekstowa';
  }

  public showPromotion($event) {
    this.setFocusedButton(event)
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
