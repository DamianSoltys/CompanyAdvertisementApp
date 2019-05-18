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
   
      $('[data-toggle="popover"]').popover({
        container: 'body'
      });
   
  }
onSubmit(){
  console.log(this.searchform.value);
}
}
