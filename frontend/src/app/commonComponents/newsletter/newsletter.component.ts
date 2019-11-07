import { Component, OnInit, AfterViewInit, AfterViewChecked, ViewChild, ElementRef, AfterContentInit, AfterContentChecked } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { NewsletterService } from 'src/app/services/newsletter.service';
import { UserREST } from 'src/app/classes/User';
import { storage_Avaliable } from 'src/app/classes/storage_checker';
import { Router, ActivatedRoute } from '@angular/router';
import { CompanyService } from 'src/app/services/company.service';
import { FormBuilder } from '@angular/forms';
import { trigger, style, animate, transition } from '@angular/animations';
import { SelectDropDownComponent } from 'ngx-select-dropdown';
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
    'Wysyłka natychmiastowa'
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
    this.typeSelect.selectItem('Informacja');
    this.formTypeSelect.selectItem('Edytor HTML');
    this.sendingTypeSelect.selectItem('Wysyłka natychmiastowa');
    this.mediaSelect.selectItem('Standardowa wysyłka newslettera');
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
    }

    if(this.sendingTypeSelect.value === 'Wysyłka natychmiastowa') {
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
