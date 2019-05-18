import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import * as $ from 'jquery';
@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {
searchform: FormGroup;
  constructor(private fb: FormBuilder) {
  this.searchform = fb.group({
   search: [''],
  });
  }

  ngOnInit() {

      $('[data-toggle=popover]').popover({
        html: true,
        trigger: 'focus',
        content: function() {
          const content = $(this).attr('data-popover-content');
          return $(content).children('.popover-body').html();
        }
      });

  }
onSubmit() {
  let searchData = this.searchform.value['search'];
  searchData = searchData.split(',');
  console.log(searchData);
}
}
