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
  public pageNumber:number = 1;
  public chunkSize:number = 3;
  public paginationTable:SectionData[][];
  public actualList:SectionData[];
  public actualIndex:number = 0;
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
    let searchData = this.searchform.value['search'];
    let subject = new Subject<any>();

    subject.subscribe(()=>{
      this.isLoaded.next(true);
    });

    searchData = searchData.split([' ',',','.']);
    this.searchS.sendSearchData(searchData).subscribe(response=>{
      this.responseBody = <SearchResponse>response.body     
      this.sectionData = this.responseBody.content;
      this.paginationTable = this.setPaginationTable(this.sectionData,3);
      let counter:number = 0;
      this.sectionData.map(data=>{
        this.searchS.getSearchSectionLogo(data).subscribe(response=>{
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
          data.logo = this.searchS.defaultSearchLogo;
          subject.next(true);
        }
      },error=>{
        data.logo = this.searchS.defaultSearchLogo;
        console.log(error);
        subject.next(true);
      });
    })
  },error=>{
    console.log(error);
    subject.next(true);
  })
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
}
