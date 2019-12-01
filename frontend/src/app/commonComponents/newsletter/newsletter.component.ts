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
import { SnackbarService, SnackbarType } from 'src/app/services/snackbar.service';
import { LoginService } from 'src/app/services/login.service';
import { MediaConnection, MediaTypeEnum, ConnectionStatus } from 'src/app/classes/Company';
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

enum MediaOptions {
  withMedia = 'Wysyłka do medii społecznościowych',
  withoutMedia = 'Standardowa wysyłka newslettera',
  onlyMedia = 'Wysyłka tylko do medii społecznościowych'
}

enum MediaType {
  FB = 'Facebook',
  TWITTER = 'Twitter',
  ALL = 'Wszystkie media'
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
  public isEdit = new BehaviorSubject(true);
  public isMedia = new BehaviorSubject(false);
  public isNewsletterList = new BehaviorSubject(false);
  public isDatePicker = new BehaviorSubject(false);
  public onlyMedia = new BehaviorSubject(false);
  public sendingTypeNow = false;
  public sendingOptions:PromotionItem;
  public type:FormType;
  public files:File[];
  public isFBConnected = false;
  public isTWITTEDConnected = false;
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
    'Standardowa wysyłka newslettera',
  ]
  public sendingTypeOptions= [
    'Wysyłka z opóźnieniem',
    'Wysyłka natychmiastowa',
    'Wysyłka na żądanie'
  ];
  public mediaTypeOptions =[ //if user connected to media TODO/display:none on select input
  ]
  public config = {
    toolbar: []
  };
  public selectConfig = {
    height: '300px',
  }
  public userREST:UserREST;
  public paramId:number;
  public textForm = this.fb.group({
    text:[''],
    title:['Podaj tytuł',[Validators.required]],
    name:['Nazwa obiektu',[Validators.required]]
  });
  public mediaForm = this.fb.group({
    text:[''],
  })
  public datePicker = this.fb.group({
    date:[],
    time:[],
  });
  constructor(private nDataService:NewsletterService,private route:ActivatedRoute,
    private cDataService:CompanyService,private router:Router,private fb:FormBuilder,private snackbar:SnackbarService,private lDataService:LoginService) {}
  @ViewChild('typeSelect') typeSelect:SelectDropDownComponent;
  @ViewChild('formTypeSelect') formTypeSelect:SelectDropDownComponent;
  @ViewChild('sendingTypeSelect') sendingTypeSelect:SelectDropDownComponent;
  @ViewChild('mediaSelect') mediaSelect:SelectDropDownComponent;
  @ViewChild('mediaTypeSelect') mediaTypeSelect:SelectDropDownComponent;

  ngOnInit() {
   this.getActualUser();
   this.checkForPermissions();
   this.checkForMediaConnection();
  }

  ngAfterViewInit() {
    setTimeout(() => {
      this.setDefaultValues();
    }, 0);
  }

  private checkForMediaConnection() {
    this.lDataService.checkIfMediaConnected(this.paramId).subscribe(response=>{
      let connectionData = <MediaConnection[]>response;
      if(connectionData.length) {
        connectionData.forEach(data=>{
          if(data.socialPlatform === MediaTypeEnum.FB && data.connectionStatus.status === ConnectionStatus.connected) {
            this.mediaTypeOptions = [...this.mediaTypeOptions,'Facebook'];      
          } else if(data.socialPlatform === MediaTypeEnum.TWITTER && data.connectionStatus.status === ConnectionStatus.connected){
            this.mediaTypeOptions = [...this.mediaTypeOptions,'Twitter'];
          }
        });

        if(connectionData.length > 1) {
          this.mediaTypeOptions = [...this.mediaTypeOptions,'Wszystkie media'];
        } 

        if(!this.mediaTypeSelect.value && this.mediaTypeOptions.length) {
          this.mediaOptions = [...this.mediaOptions,'Wysyłka do medii społecznościowych','Wysyłka tylko do medii społecznościowych'];
          this.isFBConnected = true;
          this.mediaTypeSelect.writeValue(this.mediaTypeOptions[0]);
        }
        
      }else {
        console.log(response);
      }
    });
  }
  private setDefaultValues() {
    this.mediaSelect.selectItem('Standardowa wysyłka newslettera');
    this.typeSelect.selectItem('Informacja');
    this.formTypeSelect.selectItem('Edytor HTML');
    this.sendingTypeSelect.selectItem('Wysyłka natychmiastowa');
  }

  private setFormVisibility() {
    if(this.mediaSelect.value === MediaOptions.onlyMedia) {
      this.onlyMedia.next(true);
      this.isText.next(false);
      this.isEdit.next(false);
    } else {
      this.onlyMedia.next(false);
      this.isText.next(false);
      this.isEdit.next(true);
    }

    if(this.formTypeSelect) {
      if(this.formTypeSelect.value === 'Edytor HTML') {
        this.isText.next(false);
        this.isEdit.next(true);
      } else {
        this.isText.next(true);
        this.isEdit.next(false);
      }
    } else {
      this.isEdit.next(false);
    }

    if(this.mediaSelect.value === MediaOptions.withoutMedia) {
      this.isMedia.next(false);  
    }else {
      this.isMedia.next(true);
      this.isText.next(false);
     if(this.formTypeSelect) {
      if(this.formTypeSelect.value === 'Formularz tekstowy') {
        this.formTypeSelect.selectItem('Edytor HTML');
       }
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
      if(this.sendingOptions.sendingStrategy === SendingStrategy.DELAYED && !this.sendingOptions.plannedSendingTime) {
        this.snackbar.open({
          message:'Podaj poprawny czas wysłania newslettera',
          snackbarType:SnackbarType.error,
        });
      } else {
        this.newsletterRequest();
      }
  }
  }

  private newsletterRequest() {
    this.nDataService.sendNewsletter(this.sendingOptions,this.files?this.files:null).subscribe(response=>{
      if(response) {
        this.snackbar.open({
          message:'Newsletter zostal pomyślnie wysłany',
          snackbarType:SnackbarType.success,
        });
      } else {
        this.snackbar.open({
          message:'Nie udalo się wysłać newslettera',
          snackbarType:SnackbarType.error,
        });
      }
    });
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
      let cleanText = this.textForm.controls.text.value.replace(/<\/?[^>]+(>|$)/g, "");
      this.sendingOptions.nonHtmlContent = cleanText;
    } else {
      this.nDataService.template.subscribe(template=>{
        this.sendingOptions.htmlContent = btoa(template);
      });
      this.nDataService.getHtmlTemplate.next(editorName);
    }
  }

  private setMediaValues() {
    if(this.isMedia.value || this.onlyMedia.value) {
      if(this.files) {
        this.sendingOptions.numberOfPhotos = this.files.length;
      } else {
        this.sendingOptions.numberOfPhotos = 0;
      }
      let cleanText = this.mediaForm.controls.text.value.replace(/<\/?[^>]+(>|$)/g, "");
      this.sendingOptions.nonHtmlContent = cleanText;

      if(this.onlyMedia.value) {
        this.sendingOptions.destinations = [];
      }

        if(this.mediaTypeSelect.value === MediaType.FB) {
          this.sendingOptions.destinations.push(Destination.FB);
        } else if(this.mediaTypeSelect.value === MediaType.TWITTER) {
          this.sendingOptions.destinations.push(Destination.TWITTER);
        } else {
          this.sendingOptions.destinations.push(Destination.TWITTER);
          this.sendingOptions.destinations.push(Destination.FB);
        }
      
    }
  }

  private setDateValues() {
    if(this.isDatePicker.value) {
      let dateForm= this.datePicker.controls.date.value;
      let timeForm = this.datePicker.controls.time.value;

      if(dateForm) {
        if(timeForm) {
          let string = `${dateForm.month}/${dateForm.day}/${dateForm.year} ${timeForm.hour}:${timeForm.minute}`;
          let time = moment(string, "M/D/YYYY H:mm").unix();
          this.sendingOptions.plannedSendingTime = time.toString();
        } else {
          let string = `${dateForm.month}/${dateForm.day}/${dateForm.year}`;
          let time = moment(string, "M/D/YYYY").unix();
          this.sendingOptions.plannedSendingTime = time.toString();
        }
      }
    }
    this.sendingOptions.sendingStrategy = SendingStrategy.DELAYED;
  }
  public showEdit() {
    if(!this.isText.value && this.isEdit.value && !this.onlyMedia.value) {
      return true;
    } else {
      return false;
    }
  }

  public showText() {
    if(this.isText.value && !this.isEdit.value && !this.onlyMedia.value) {
      return true;
    } else {
      return false;
    }
  }

  public showTitle() {
    if(!this.isMedia.value) {
      return true;
    } else if(this.isMedia.value && this.isEdit.value && !this.onlyMedia.value) {
      return true;
    } else {
      return false;
    }
  }

  public showMediaTypeSelect() {
    if(this.isMedia.value || this.onlyMedia.value) {
      return true;
    } else {
      return false;
    }
  }
  public isAnyMediaConnected() {
    if(this.isFBConnected || this.isTWITTEDConnected) {
      return true;
    } else {
      return false;
    }
  }
  
  public goBack() {
    this.router.navigate(['/companyProfile',this.paramId]);
  }

}
