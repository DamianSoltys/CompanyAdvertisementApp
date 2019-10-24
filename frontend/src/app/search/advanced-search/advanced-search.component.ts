import { Component, OnInit, ViewChild } from '@angular/core';
import { SearchService } from 'src/app/services/search.service';
import { FormBuilder } from '@angular/forms';

@Component({
  selector: 'app-advanced-search',
  templateUrl: './advanced-search.component.html',
  styleUrls: ['./advanced-search.component.scss']
})

export class AdvancedSearchComponent implements OnInit {
  public categoryOptions:string[] = [
    'Usługi transportowe',
    'Usługi medyczne',
    'Mechanika samochodowa',
    'Naprawa sprzętu domowego',
    'Ogrodnictwo',
    'Usługi opieki',
    'Usługi kosmetyczne',
    'Usługi informatyczne',
    'Sprzedaż produktów',
  ];
  public typeOptions:string[] = [
    'Firma',
    'Zakład'
  ]
  public voivodeshipOptions:string[] = [
    'dolnośląskie',
    'kujawsko-pomorskie',
    'lubelskie',
    'lubuskie',
    'łódzkie',
    'małopolskie',
    'mazowieckie',
    'opolskie',
    'podkarpackie',
    'podlaskie',
    'pomorskie',
    'śląskie',
    'świętokrzyskie',
    'warmińsko-mazurskie',
    'wielkopolskie',
    'zachodniopomorskie'
  ]
  public cityOptions:string[] =[
    
  ]
  public config = {
  }

  public searchForm = this.fb.group({
    type:[''],
    voivodeship:[''],
    city:[''],
    category:[''],
    name:[''],
  });

  constructor(private sDataService:SearchService,private fb:FormBuilder) { }

  ngOnInit() {
    

  }

  public getSearchData() {
    console.log(this.searchForm.value)
  }

}
