import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import * as $ from 'jquery';
import { SearchService } from '../services/search.service';
@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {
searchform: FormGroup;
  constructor(private fb: FormBuilder,private searchS:SearchService) {
  this.searchform = fb.group({
   search: [''],
  });
  }

  ngOnInit() {

      $('[data-toggle=popover]').popover({
        html: true,
        trigger: 'focus',
        delay: { "show": 100, "hide": 100 },
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
  searchData = searchData.split(',');
  this.searchS.sendSearchData(searchData);
}
}
