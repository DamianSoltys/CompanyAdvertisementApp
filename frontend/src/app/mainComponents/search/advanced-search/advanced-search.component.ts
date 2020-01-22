import { Component, OnInit, ViewChild } from '@angular/core';
import { SearchService } from 'src/app/services/search.service';
import { FormBuilder } from '@angular/forms';
import { TouchSequence } from 'selenium-webdriver';
import { SnackbarService, SnackbarType } from 'src/app/services/snackbar.service';
import { SearchResponse, SectionData } from 'src/app/classes/Section';
import { Subject, BehaviorSubject } from 'rxjs';
import { Paginator } from '../search.component';
import { CompanyService } from 'src/app/services/company.service';
import { categories } from '../../../classes/Category';

export interface AdvSearchData {
  name?: string;
  type?: string[];
  category?: string[];
  voivodeship?: string[];
  city?: string[];
}
@Component({
  selector: 'app-advanced-search',
  templateUrl: './advanced-search.component.html',
  styleUrls: ['./advanced-search.component.scss']
})

export class AdvancedSearchComponent implements OnInit {

  public categoryOptions: string[];
  public typeOptions = [
    'Firma',
    'Zakład'
  ];
  public voivodeshipOptions: string[] = [
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
  ];
  public cityOptions: string[] = [

  ];
  public config = {
  };
  public searchForm = this.fb.group({
    type: [''],
    voivodeship: [''],
    city: [''],
    category: [''],
    name: [''],
  });
  public cityArray;
  public companies: any[];
  public branches: any[];
  public sectionData: SectionData[];
  public responseBody: SearchResponse;
  public isLoaded = new BehaviorSubject(false);
  public pageNumber: number = 0;
  public actualList: Paginator
  public actualIndex: number = 0;
  public dataNumber: number;
  public companyNumber: number;
  public branchNumber: number;
  public searchData: any;
  public resultText: string;

  constructor(
    private sDataService: SearchService,
    private fb: FormBuilder,
    private snackbarService: SnackbarService,
    private cDataService: CompanyService) { }

  ngOnInit() {
    this.getCityData();
    this.getCategoryData();
  }

  public getSearchData() {
    this.searchData = this.searchForm.value;
    let subject = new Subject<any>();

    if (this.searchData.name) {
      let string = this.searchForm.value['name'];
      let nameArray = string.split(' ');
      this.searchData.name = nameArray;
    }
    subject.subscribe(() => {
      this.isLoaded.next(true);
    });
    this.sDataService.sendAdvSearchData(this.searchData).subscribe(response => {
      this.responseBody = <SearchResponse>response.body
      this.sectionData = this.responseBody.result;
      this.sDataService.engToPl(this.sectionData);
      this.dataNumber = this.responseBody.branchesNumber + this.responseBody.companiesNumber;
      this.actualList = this.sDataService.paginator(this.sectionData, 1, 3);
      this.resultText = this.resultText = this.sDataService.polishPlural('wynik', 'wyniki', 'wyników', this.dataNumber);

      if (this.sectionData) {
        this.getImages();
      } else {
        this.sectionData = undefined;
        subject.next(true);
      }

    }, error => {
      console.log(error);
      subject.next(true);
    })
  }

  public getImages() {
    this.sectionData.map(data => {
      this.sDataService.getSearchSectionLogo(data).subscribe(response => {
        if (response.status != 204) {
          let reader = new FileReader();
          reader.addEventListener("load", () => {
            data.logo = reader.result;

            return true;
          }, false);

          if (response.body) {
            reader.readAsDataURL(<any>response.body);
          }
        } else {
          data.logo = this.sDataService.defaultSearchLogo;

          return true;
        }
      }, error => {
        data.logo = this.sDataService.defaultSearchLogo;

        return true;
      });
    });
  }

  public getCityData() {
    this.sDataService.getCitiesByVoivodeship().subscribe(response => {
      this.cityArray = response.body;
    }, error => {
      console.log(error);
      this.snackbarService.open({
        snackbarType: SnackbarType.error,
        message: 'Nie udało się pobrać listy miast!',
      })
    });
  }

  public checkForCity() {
    let voivodeship = this.cityArray[this.searchForm.controls.voivodeship.value];

    if (voivodeship) {
      if (voivodeship.length) {
        voivodeship.forEach(element => {
          this.cityOptions = [...this.cityOptions, element];
        });
      } else {
        this.cityOptions = [];
      }
    }
  }


  public previousPage() {
    if (this.actualList.pre_page) {
      this.actualList = this.sDataService.paginator(this.sectionData, this.actualList.pre_page, 3);
    }
  }

  public nextPage() {
    if (this.actualList.next_page) {
      this.actualList = this.sDataService.paginator(this.sectionData, this.actualList.next_page, 3);
    }
  }

  public showEmptyMessage() {
    if (this.isLoaded.value && !this.sectionData) {
      return true;
    } else if (this.isLoaded.value && this.sectionData) {
      return false;
    }
  }

  private getCategoryData() {
    this.cDataService.getCategoryData().subscribe(data => {
      if (data) {
        this.categoryOptions = data.body;
      } else {
        this.categoryOptions = categories;
      }
    });
  }
}
