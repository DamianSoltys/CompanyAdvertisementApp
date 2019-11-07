import { Component, OnInit, AfterViewInit, AfterViewChecked } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { NewsletterService } from 'src/app/services/newsletter.service';
import { UserREST } from 'src/app/classes/User';
import { storage_Avaliable } from 'src/app/classes/storage_checker';
import { Router, ActivatedRoute } from '@angular/router';
import { CompanyService } from 'src/app/services/company.service';
import { FormBuilder } from '@angular/forms';
import { trigger, style, animate, transition } from '@angular/animations';
enum FormType {
  info,
  promotion,
  product,
  text
}
@Component({
  selector: 'app-newsletter',
  templateUrl: './newsletter.component.html',
  styleUrls: ['./newsletter.component.scss'],
  animations: [
    trigger(
      'heightAnimate', [
        transition(':enter', [
          style({height:'0px',overflow:'hidden'}),
          animate('200ms', style({height:'*',overflow:'hidden'}))
        ]),
        transition(':leave', [
          style({height:'*',overflow:'hidden'}),
          animate('200ms', style({height:'0px',overflow:'hidden'}))
        ])
      ]
    ),
    trigger(
      'opacityAnimate', [
        transition(':enter', [
          style({opacity:0}),
          animate('200ms', style({opacity:1}))
        ]),
        transition(':leave', [
          style({opacity:1}),
          animate('200ms', style({opacity:0}))
        ])
      ]
    )
  ],
})
export class NewsletterComponent implements OnInit,AfterViewInit{
  public isInfo = new BehaviorSubject(false);
  public isPromotion = new BehaviorSubject(false);
  public isProduct = new BehaviorSubject(false);
  public isText = new BehaviorSubject(false);
  public isMedia = new BehaviorSubject(false);
  public isNewsletterList = new BehaviorSubject(false);
  public isDatePicker = new BehaviorSubject(false);
  public type:FormType;
  public typeButton:string = 'Wersja tekstowa';
  public mediaButton:string = 'Pokaż formularz medii społecznościowych';
  public datePickerButton:string = 'Wybierz czas wysłania newslettera';
  public files:File[];
  public config = {
    toolbar: [['bold', 'italic', 'underline']]
  };
  public userREST:UserREST;
  public paramId:number
  public textForm = this.fb.group({
    text:[''],
  });
  public mediaForm = this.fb.group({
    text:[''],
  })
  public datePicker = this.fb.group({
    date:[],
    time:[],
  });
  constructor(private nDataService:NewsletterService,private route:ActivatedRoute,private cDataService:CompanyService,private router:Router,private fb:FormBuilder) {}

  ngOnInit() {
   this.getActualUser();
   this.checkForPermissions();
  }
  ngAfterViewInit() {
   setTimeout(() => {
     this.setFirstFocused();
   }, 0);
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
    this.typeButton = 'Wyświetl formularz';
  }

  public showDatePicker() {
    this.isDatePicker.next(!this.isDatePicker.value);
    if(this.isDatePicker.value) {
      this.datePickerButton = 'Schowaj';
    } else {
      this.datePickerButton = 'Wybierz czas wysłania newslettera';
    }
  }

  public showInfo($event?:Event) {
    this.setFocusedButton(event);
    this.nDataService.destroyEditor.next(true);
    this.isInfo.next(true);
    this.isProduct.next(false);
    this.isPromotion.next(false);
    this.isText.next(false);
    this.type = FormType.info;
    this.typeButton = 'Wyświetl formularz';
  }
  
  public showProduct($event?:Event) {
    this.setFocusedButton(event);
    this.nDataService.destroyEditor.next(true);
    this.isInfo.next(false);
    this.isProduct.next(true);
    this.isPromotion.next(false);
    this.isText.next(false);
    this.type = FormType.product;
    this.typeButton = 'Wyświetl formularz';
  }

  public showPromotion($event?:Event) {
    this.setFocusedButton(event);
    this.nDataService.destroyEditor.next(true);
    this.isInfo.next(false);
    this.isProduct.next(false);
    this.isPromotion.next(true);
    this.isText.next(false);
    this.type = FormType.promotion;
    this.typeButton = 'Wyświetl formularz';
  }

  public showNewsletterList() {
    this.isNewsletterList.next(!this.isNewsletterList.value);
  }

  public showTextForm(type:FormType) {
    if(this.isText.value) {
      this.isText.next(false);
      this.typeButton = 'Wyświetl formularz';
      console.log(type);
    } else {
      this.typeButton = 'Wyświetl edytor';
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

  public onDateSelect($event) {
   
  }

  public onFileSelected(event) {
    if(!this.files) {
      this.files = [];
      this.files = event.target.files;
    } else {
      this.files = event.target.files;
    }
    console.log(this.files);
  }

  public showMediaForm() {
    if(this.showTextButton()) {
      if(this.isMedia.value) {
        this.isMedia.next(false);
        this.mediaButton = 'Pokaż formularz medii społecznościowych';
      } else {
        this.isMedia.next(true);
        this.mediaButton = 'Schowaj formularz medii społecznościowych';
      }
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

  public sendNewsletter(editorName?:string) {
    if(this.isText.value) {
      console.log(this.textForm.value);
    } else {
      this.nDataService.template.subscribe(template=>{
        console.log(template);
      });
      this.nDataService.getHtmlTemplate.next(editorName);
    }

    if(this.isMedia.value) {
      console.log(this.mediaForm.value)
      console.log(this.files)
    }

    console.log(this.datePicker.value)
  }
  

}
