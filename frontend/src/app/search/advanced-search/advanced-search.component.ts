import { Component, OnInit, ViewChild } from '@angular/core';
import { SearchService } from 'src/app/services/search.service';
import { FormBuilder } from '@angular/forms';
import { TouchSequence } from 'selenium-webdriver';
import { SnackbarService, SnackbarType } from 'src/app/services/snackbar.service';
import { SearchResponse, SectionData } from 'src/app/classes/Section';
import { Subject, BehaviorSubject } from 'rxjs';

export interface AdvSearchData {
  name?;
  type?:string[];
  category?:string[];
  voivodeship?:string[];
  city?:string[];
}
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
  public typeOptions = [
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
  public companies:any[];
  public branches:any[];
  public sectionData:SectionData[];
  public responseBody:SearchResponse;
  public isLoaded = new BehaviorSubject(false);
  public pageNumber:number = 1;
  public chunkSize:number = 3;
  public paginationTable:SectionData[][];
  public actualList:SectionData[];
  public actualIndex:number = 0;
  constructor(private sDataService:SearchService,private fb:FormBuilder,private snackbarService:SnackbarService) { }

  ngOnInit() {
    this.getCityData();

  }

  public getSearchData() {
    let searchData:AdvSearchData = this.searchForm.value;
    let subject = new Subject<any>();
    if(searchData.name) {
      let string = this.searchForm.value['name'];
      let nameArray = string.split(' ');
      searchData.name = nameArray;
    }


    subject.subscribe(()=>{
      this.isLoaded.next(true);
    });

    this.sDataService.sendAdvSearchData(searchData).subscribe(response=>{
      this.responseBody = <SearchResponse>response.body     
      this.sectionData = this.responseBody.content;
      if(this.sectionData.length) {
        this.paginationTable = this.setPaginationTable(this.sectionData,3);
        let counter:number = 0;
        this.sectionData.forEach(data=>{
          this.sDataService.getSearchSectionLogo(data).subscribe(response=>{
            console.log(response)
            if(response.status != 204) {
              let reader = new FileReader();
              reader.addEventListener("load", () => {
                data.logo = reader.result;
                subject.next(true);
            }, false);
  
            if (response.body) {
                reader.readAsDataURL(<any>response.body);
            }
          }else {
            data.logo = this.sDataService.defaultSearchLogo;
            subject.next(true);
          }
        },error=>{
          data.logo = this.sDataService.defaultSearchLogo;
          console.log(error);
          subject.next(true);
        });
      });
      }else {
        this.sectionData = undefined;
        subject.next(true);
      }
     
  },error=>{
    console.log(error);
    subject.next(true);
  })
  }

  public getCityData() {
    this.sDataService.getCitiesByVoivodeship().subscribe(response=>{
      this.cityArray = response.body;
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
    if(voivodeship) {
      if(voivodeship.length) {
        voivodeship.forEach(element => {
          this.cityOptions = [...this.cityOptions,element];
        });
      } else {
        this.cityOptions = [];
      }
    }
  }

  public setPaginationTable(myArray, chunk_size){
    var index = 0;
    var arrayLength = myArray.length;
    var tempArray = [];
    
    for (index = 0; index < arrayLength; index += chunk_size) {
        this.chunkSize = myArray.slice(index, index+chunk_size);
        tempArray.push(this.chunkSize);
    }
    this.actualList = tempArray[0];
    return tempArray;
  }
  
  public previousPage() {
    if(this.actualIndex != 0) {
      this.actualList = this.paginationTable[--this.actualIndex]
      this.pageNumber--;
  
    }
  }
  
  public nextPage() {
    if(this.paginationTable) {
      if(this.paginationTable.length-1 > this.actualIndex) {
        this.actualList = this.paginationTable[++this.actualIndex]
        this.pageNumber++;
  
      }
    }
  }
  
  public showEmptyMessage() {

    if(this.isLoaded.value && !this.sectionData) {
      return true;
    } else if(this.isLoaded.value && this.sectionData){
      return false;
    }
  }
}
