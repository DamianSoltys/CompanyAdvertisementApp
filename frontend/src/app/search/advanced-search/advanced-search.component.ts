import { Component, OnInit, ViewChild } from '@angular/core';
import { SearchService } from 'src/app/services/search.service';
import { FormBuilder } from '@angular/forms';
import { TouchSequence } from 'selenium-webdriver';
import { SnackbarService, SnackbarType } from 'src/app/services/snackbar.service';

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

  public cityArray;

  constructor(private sDataService:SearchService,private fb:FormBuilder,private snackbarService:SnackbarService) { }

  ngOnInit() {
    this.getCityData();

  }

  public getSearchData() {
   
  }

  public getCityData() {
    this.sDataService.getCitiesByVoivodeship().subscribe(response=>{
      this.cityArray = response.body;
      console.log(this.cityArray)
    },error=>{
      console.log(error);
      this.snackbarService.open({
        snackbarType:SnackbarType.error,
        message:'Nie udało się pobrać listy miast!',
      })
    });
  }

  public checkForCity() {
    let voivodeship = this.cityArray[this.searchForm.controls.voivodeship.value];
    if(voivodeship.length) {
      voivodeship.forEach(element => {
        this.cityOptions = [...this.cityOptions,element];
      });
    } else {
      this.cityOptions = [];
    }
  }
}
