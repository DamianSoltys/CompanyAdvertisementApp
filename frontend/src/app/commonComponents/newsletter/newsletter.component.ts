import { Component, OnInit, AfterViewInit, AfterViewChecked, ViewChild, ElementRef, AfterContentInit, AfterContentChecked } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { NewsletterService } from 'src/app/services/newsletter.service';
import { UserREST } from 'src/app/classes/User';
import { storage_Avaliable } from 'src/app/classes/storage_checker';
import { Router, ActivatedRoute } from '@angular/router';
import { CompanyService } from 'src/app/services/company.service';
import { FormBuilder, Validators } from '@angular/forms';
import { trigger, style, animate, transition } from '@angular/animations';
import { SelectDropDownComponent } from 'ngx-select-dropdown';
import  * as moment from 'moment';
import { PromotionItem, SendingStrategy, Destination, PromotionType } from 'src/app/classes/Newsletter';
enum FormType {
  info = 'Informacja',
  product = 'Produkt',
  promotion = 'Promocja'
}

enum SendStrategy {
  delayed = 'Wysyłka z opóźnieniem',
  now = 'Wysyłka natychmiastowa',
  at_will = 'Wysyłka na żądanie'
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
  public sendingTypeNow = false;
  public sendingOptions:PromotionItem;
  public type:FormType;
  public files:File[];
  public typeOptions =[
    'Informacja',
    'Produkt',
    'Promocja'
  ]
  public formTypeOptions = [
    'Formularz tekstowy',
    'Edytor HTML'
  ]
  public mediaOptions = [
    'Wysyłka do medii społecznościowych',
    'Standardowa wysyłka newslettera'
  ]
  public sendingTypeOptions= [
    'Wysyłka z opóźnieniem',
    'Wysyłka natychmiastowa',
    'Wysyłka na żądanie'
  ]
  public config = {
    toolbar: [['bold', 'italic', 'underline']]
  };
  public selectConfig = {
    height: '300px',
  }
  public userREST:UserREST;
  public paramId:number;
  public textForm = this.fb.group({
    text:[''],
    title:['',[Validators.required]],
    name:['',[Validators.required]]
  });
  public mediaForm = this.fb.group({
    text:[''],
  })
  public datePicker = this.fb.group({
    date:[],
    time:[],
  });
  constructor(private nDataService:NewsletterService,private route:ActivatedRoute,private cDataService:CompanyService,private router:Router,private fb:FormBuilder) {}
  @ViewChild('typeSelect') typeSelect:SelectDropDownComponent;
  @ViewChild('formTypeSelect') formTypeSelect:SelectDropDownComponent;
  @ViewChild('sendingTypeSelect') sendingTypeSelect:SelectDropDownComponent;
  @ViewChild('mediaSelect') mediaSelect:SelectDropDownComponent;

  ngOnInit() {
   this.getActualUser();
   this.checkForPermissions();
   
  }
  ngAfterViewInit() {
    setTimeout(() => {
      this.setDefaultValues();
    }, 0);
  }
  private setDefaultValues() {
    this.mediaSelect.selectItem('Standardowa wysyłka newslettera');
    this.typeSelect.selectItem('Informacja');
    this.formTypeSelect.selectItem('Edytor HTML');
    this.sendingTypeSelect.selectItem('Wysyłka natychmiastowa');
  }

  private setFormVisibility() {
    if(this.formTypeSelect.value === 'Edytor HTML') {
      this.isText.next(false);
    } else {
      this.isText.next(true);
    }

    if(this.mediaSelect.value === 'Standardowa wysyłka newslettera') {
      this.isMedia.next(false);  
    }else {
      this.isMedia.next(true);
      this.isText.next(false);
     if(this.formTypeSelect.value === 'Formularz tekstowy') {
      this.formTypeSelect.selectItem('Edytor HTML');
     }
    }

    if(this.sendingTypeSelect.value === SendStrategy.now || this.sendingTypeSelect.value === SendStrategy.at_will ) {
      this.isDatePicker.next(false);
    } else {
      this.isDatePicker.next(true);
    }
  }

  public getActualUser() {
    if(storage_Avaliable('localStorage')) {
      if(localStorage.getItem('userREST')) {
        this.userREST = JSON.parse(localStorage.getItem('userREST'));
      }
    }
  }

  public checkForChanges(element:SelectDropDownComponent) {
    this.setFormVisibility();

    if(!element.value) {
      element.selectItem(element.options[0]);
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

  public sendNewsletter(editorName?:string) {
    this.setStandardValues();
    this.setSendingTypeValues();
    this.setTypeValues();
    this.setContentValues(editorName);
    this.setDateValues();
    this.setMediaValues();
  
    console.log(this.sendingOptions);

    if(this.sendingOptions) {
      if(!this.isMedia.value) {
        this.nDataService.sendNewsletter(this.sendingOptions).subscribe(response=>{
          console.log(response);
        });
      } else {
        this.nDataService.sendMediaNewsletter(this.sendingOptions).subscribe(response=>{
          console.log(response);
        });
      }
    }
  }
  private setStandardValues() {
    this.sendingOptions = {};
    this.sendingOptions.destinations = [];

    this.sendingOptions.companyId = this.paramId;
    this.sendingOptions.destinations.push(Destination.NEWSLETTER);
    this.sendingOptions.emailTitle = this.textForm.controls.title.value;
    this.sendingOptions.name = this.textForm.controls.name.value;
  }

  private setTypeValues() {
    switch (this.typeSelect.value) {
      case FormType.info:{
        this.sendingOptions.promotionItemType = PromotionType.INFORMATION;
        break;
      }
      case FormType.product:{
        this.sendingOptions.promotionItemType = PromotionType.PRODUCT;
        break;
      }
      case FormType.promotion:{
        this.sendingOptions.promotionItemType = PromotionType.PROMOTION;
        break;
      }
    }
  }
  private setSendingTypeValues() {
    switch (this.sendingTypeSelect.value) {
      case  SendStrategy.now: {
        this.sendingOptions.sendingStrategy = SendingStrategy.AT_CREATION;
        break;
      }
      case  SendStrategy.at_will: {
        this.sendingOptions.sendingStrategy = SendingStrategy.AT_WILL;
        break;
      }
    }
  }

  private setContentValues(editorName:string) {
    if(this.isText.value) {
      this.sendingOptions.nonHtmlContent = this.textForm.controls.text.value;
    } else {
      this.nDataService.template.subscribe(template=>{
        this.sendingOptions.htmlContent = btoa(template);
      });
      this.nDataService.getHtmlTemplate.next(editorName);
    }
  }

  private setMediaValues() {
    if(this.isMedia.value) {
      console.log(this.mediaForm.value)
      console.log(this.files)
      this.sendingOptions.numberOfPhotos = this.files.length;
      this.sendingOptions.nonHtmlContent = this.mediaForm.controls.text.value;
      this.sendingOptions.destinations.push(Destination.FB);
    }
  }

  private setDateValues() {
    if(this.isMedia.value) {
      let dateForm= this.datePicker.controls.date.value;
      let timeForm = this.datePicker.controls.time.value;

      if(dateForm) {
        if(timeForm) {
          let string = `${dateForm.month}/${dateForm.day}/${dateForm.year} ${timeForm.hour}:${timeForm.minute}`;
          let time = moment(string, "M/D/YYYY H:mm").unix();
          this.sendingOptions.plannedSendingTime = time.toString();
          this.sendingOptions.sendingStrategy = SendingStrategy.DELAYED;
          console.log(time);
        } else {
          let string = `${dateForm.month}/${dateForm.day}/${dateForm.year}`;
          let time = moment(string, "M/D/YYYY").unix();
          this.sendingOptions.plannedSendingTime = time.toString();
          this.sendingOptions.sendingStrategy = SendingStrategy.DELAYED;
          console.log(time);
        }
      }
    }
  }
  
  public goBack() {
    this.router.navigate(['/companyProfile',this.paramId]);
  }

}
