import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import * as $ from 'jquery';
import { SearchService } from '../services/search.service';
import { SearchResponse, SectionData } from '../classes/Section';
import { Subject, BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {
  searchform: FormGroup;

  public companies:any[];
  public branches:any[];
  public sectionData:SectionData[];
  public responseBody:SearchResponse;
  public isLoaded = new BehaviorSubject(false);
  public pageNumber:number = 0;
  public actualList:SectionData[];
  public actualIndex:number = 0;
  public companyNumber:number;
  public branchNumber:number;
  public searchData:any;
  public totalPages:number;
  public isEmptyMessage = new BehaviorSubject(false);
  constructor(private fb: FormBuilder, private searchS: SearchService) {
    this.searchform = fb.group({
      search: ['']
    });
  }

  ngOnInit() {
    $('[data-toggle=popover]').popover({
      html: true,
      trigger: 'focus',
      delay: { show: 100, hide: 100 },
      template: `<div class="popover" role="tooltip">
                    <div class="arrow"></div>
                    <h3 class="popover-header"></h3>
                    <div class="popover-body text-center"></div>
                  </div>`,
      content: function() {
        const content = $(this).attr('data-popover-content');
        const textElement = $(content).children('.popover-body');
        return textElement.html();
      }
    });
  }
  
  onSubmit() {
    this.searchData = this.searchform.value['search'];
    let subject = new Subject<any>();

    subject.subscribe(()=>{
      this.isLoaded.next(true);
    });

   if(this.searchData) {
    this.searchData = this.searchData.split([' ',',','.']);
   }
    this.companyNumber = 0;
    this.branchNumber = 0;
    
    this.searchS.sendSearchData(this.searchData).subscribe(response=>{
      console.log(response)
      this.responseBody = <SearchResponse>response.body  
      this.totalPages = this.responseBody.result.totalPages;  
      this.sectionData = this.responseBody.result.content;
      this.actualList = this.responseBody.result.content;
      this.isEmptyMessage.next(this.sectionData.length?false:true);
      subject.next(this.getImages(true));
  },error=>{
    console.log(error);
    subject.next(true);
  })
}

public getImages(firstRequest?:boolean) {
  this.actualList.map(data=>{
    this.searchS.getSearchSectionLogo(data).subscribe(response=>{
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
      data.logo = this.searchS.defaultSearchLogo;
      return true;
    }
  },error=>{
    data.logo = this.searchS.defaultSearchLogo;
    console.log(error);
    return true;
  });
});
}

public previousPage() {
  if(this.pageNumber > 0 && this.searchData) {
    this.searchS.getActualPageData(this.searchData,--this.pageNumber).subscribe(response=>{
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
    this.searchS.getActualPageData(this.searchData,++this.pageNumber).subscribe(response=>{
    let searchResponse = <SearchResponse>response.body;
    this.actualList = searchResponse.result.content;
    this.getImages();
  },error=>{
    console.log(error);
  });
}
}

public showEmptyMessage() {
  if(this.isLoaded.value && this.isEmptyMessage.value) {
    return true;
  } else {
    return false
  }
}

}
