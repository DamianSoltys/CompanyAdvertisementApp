import { Component, OnInit, ViewChild } from '@angular/core';
import { SearchService } from 'src/app/services/search.service';
import { FormBuilder } from '@angular/forms';
import { TouchSequence } from 'selenium-webdriver';
import { SnackbarService, SnackbarType } from 'src/app/services/snackbar.service';
import { SearchResponse, SectionData } from 'src/app/classes/Section';
import { Subject, BehaviorSubject } from 'rxjs';

export interface AdvSearchData {
  name?:string;
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
  public pageNumber:number = 0;
  public paginationTable:SectionData[][];
  public actualList:SectionData[];
  public actualIndex:number = 0;
  public dataNumber:number;
  public companyNumber:number;
  public branchNumber:number;
  public searchData:any;
  public totalPages:number;
  constructor(private sDataService:SearchService,private fb:FormBuilder,private snackbarService:SnackbarService) { }

  ngOnInit() {
    this.getCityData();

  }

  public getSearchData() {
    this.searchData = this.searchForm.value;
    let subject = new Subject<any>();
    if(this.searchData.name) {
      let string = this.searchForm.value['name'];
      let nameArray = string.split(' ');
      this.searchData.name = nameArray;
    }


    subject.subscribe(()=>{
      this.isLoaded.next(true);
    });

    this.sDataService.sendAdvSearchData(this.searchData).subscribe(response=>{
      console.log(response)
      this.responseBody = <SearchResponse>response.body     
      this.sectionData = this.responseBody.result.content;
      this.totalPages = this.responseBody.result.totalPages;  
      this.sectionData = this.responseBody.result.content;
      this.actualList = this.responseBody.result.content;
      this.dataNumber = this.responseBody.result.totalElements;
      if(this.sectionData) {
        this.getImages();
      }else {
        this.sectionData = undefined;
        subject.next(true);
      }
     
  },error=>{
    console.log(error);
    subject.next(true);
  })
  }

  public getImages() {
    this.actualList.map(data=>{
      this.sDataService.getSearchSectionLogo(data).subscribe(response=>{
        if(response.status != 204) {
          let reader = new FileReader();
          reader.addEventListener("load", () => {
            data.logo = reader.result;
            return true;
        }, false);
  
        if (response.body) {
            reader.readAsDataURL(<any>response.body);
        }
      }else {
        data.logo = this.sDataService.defaultSearchLogo;
        return true;
      }
    },error=>{
      data.logo = this.sDataService.defaultSearchLogo;
      return true;
    });
  });
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

  
  public previousPage() {
    if(this.pageNumber > 0 && this.searchData) {
      this.sDataService.getActualAdvSearchPage(this.searchData,--this.pageNumber).subscribe(response=>{
        console.log(response)
        let searchResponse = <SearchResponse>response.body;
        this.actualList = searchResponse.result.content;
        this.getImages();
    },error=>{
      console.log(error);
    });
  }
  }
  
  public nextPage() {
    if((this.pageNumber < this.totalPages-1) && this.searchData) {
      this.sDataService.getActualAdvSearchPage(this.searchData,++this.pageNumber).subscribe(response=>{
        console.log(response)
      let searchResponse = <SearchResponse>response.body;
      this.actualList = searchResponse.result.content;
      this.getImages();
    },error=>{
      console.log(error);
    });
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
